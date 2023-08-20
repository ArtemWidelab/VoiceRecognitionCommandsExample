package ua.widelab.voicerecognitioncommandsexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import ua.widelab.audio_recording.AudioRecording
import ua.widelab.main_commands.repo.CommandProcessor
import ua.widelab.voicerecognitioncommandsexample.ui.theme.ExampleTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var commandProcessor: CommandProcessor

    @Inject
    lateinit var audioRecording: AudioRecording

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleTheme {
                Column {
                    val commandProcessorState by commandProcessor.result.collectAsState(initial = emptyList())
                    val audioRecordingState by audioRecording.results.collectAsState(initial = "")
                    val isRecordingState by audioRecording.isListening.collectAsState(initial = false)
                    var word by remember { mutableStateOf("") }
                    OutlinedTextField(value = word, onValueChange = {
                        word = it
                    })
                    Button(onClick = {
                        commandProcessor.pushWord(word)
                        word = ""
                    }) {
                        Text(text = "ADD!")
                    }
                    Text(text = "Audio: $isRecordingState - $audioRecordingState")
                    Text(text = "Commands:")
                    LazyColumn(reverseLayout = true) {
                        items(commandProcessorState) {
                            Text(text = it.toString())
                        }
                    }
                }
                /*val audioPermissionState =
                    rememberPermissionState(permission = android.Manifest.permission.RECORD_AUDIO)
                if (audioPermissionState.status.isGranted) {
                    MainScreen()
                } else {
                    Button(onClick = { audioPermissionState.launchPermissionRequest() }) {
                        Text(text = "click")
                    }
                }*/
                DisposableEffect(Unit) {
                    audioRecording.start()
                    onDispose {
                        audioRecording.stop()
                    }
                }
            }
        }
    }
}

/*
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val audioRecording = remember {
        SpeechRecognizerAudioRecording(context)
    }
    DisposableEffect(Unit) {
        audioRecording.start()
        onDispose {
            audioRecording.stop()
        }
    }

    val audioState by audioRecording.getResults().collectAsState(initial = "EMPTY")
    Text(text = "State = $audioState")
}*/
