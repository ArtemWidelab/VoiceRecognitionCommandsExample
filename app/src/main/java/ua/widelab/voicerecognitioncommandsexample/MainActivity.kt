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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import ua.widelab.main_commands.repo.CommandProcessor
import ua.widelab.voicerecognitioncommandsexample.ui.theme.ExampleTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var commandProcessor: CommandProcessor

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleTheme {
                Column {
                    val state by commandProcessor.result.collectAsState(initial = emptyList())
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
                    LazyColumn(reverseLayout = true) {
                        items(state) {
                            Text(text = it.toString())
                        }
                    }
                }
            }
        }
    }
}