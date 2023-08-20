package ua.widelab.main_commands.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.widelab.main_commands.entities.CommandResult
import ua.widelab.main_commands.presentation.MainCommandsViewModel

@Composable
fun CommandsScreen(
    viewModel: MainCommandsViewModel = viewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    CommandsScreen(
        isRecording = state.isRecording,
        commands = state.commands,
        startRecording = viewModel::startRecording,
        stopRecording = viewModel::stopRecording
    )
}

@Composable
fun CommandsScreen(
    isRecording: Boolean,
    commands: List<CommandResult>,
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
) {
    Column {
        Text(text = "I'm HER - $isRecording")
        LazyColumn(reverseLayout = true) {
            items(commands) {
                Text(text = it.toString())
            }
        }
        DisposableEffect(Unit) {
            startRecording()
            onDispose {
                stopRecording()
            }
        }
    }
}