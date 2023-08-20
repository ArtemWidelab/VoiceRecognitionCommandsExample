package ua.widelab.voicerecognitioncommandsexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ua.widelab.main_commands.compose.MainCommandsScreen
import ua.widelab.voicerecognitioncommandsexample.ui.theme.ExampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleTheme {
                MainCommandsScreen()
            }
        }
    }
}