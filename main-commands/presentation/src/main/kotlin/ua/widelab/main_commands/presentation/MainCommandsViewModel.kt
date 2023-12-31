package ua.widelab.main_commands.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.widelab.audio_recording.AudioRecorder
import ua.widelab.main_commands.repo.CommandProcessor
import javax.inject.Inject

@HiltViewModel
class MainCommandsViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val commandProcessor: CommandProcessor
) : ViewModel() {

    data class State(
        val isRecording: Boolean = false,
        val commands: List<Command> = emptyList(),
    )

    private val mutableState = MutableStateFlow(State())
    val stateFlow = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            audioRecorder.results.collect {
                commandProcessor.pushWord(it)
            }
        }
        viewModelScope.launch {
            audioRecorder.isListening.collectLatest { recording ->
                mutableState.update {
                    it.copy(
                        isRecording = recording
                    )
                }
            }
        }
        viewModelScope.launch {
            commandProcessor.result.collectLatest { result ->
                mutableState.update {
                    val newIds = result.map { it.id }
                    val toDeleteCommands = it.commands
                        .filter { it.id !in newIds && !it.isDeleting }
                        .map { it.copy(isDeleting = true, isCurrent = false) }
                    val newCommands = result.map {
                        Command(
                            id = it.id,
                            name = it.name,
                            value = it.value,
                            isCurrent = it.isCurrent,
                            isDeleting = false
                        )
                    }
                    it.copy(
                        commands = toDeleteCommands + newCommands.reversed()
                    )
                }
            }
        }
    }

    fun startRecording() {
        audioRecorder.start()
    }

    fun stopRecording() {
        audioRecorder.stop()
    }

}

data class Command(
    val id: String,
    val name: String,
    val value: String,
    val isCurrent: Boolean,
    val isDeleting: Boolean
)