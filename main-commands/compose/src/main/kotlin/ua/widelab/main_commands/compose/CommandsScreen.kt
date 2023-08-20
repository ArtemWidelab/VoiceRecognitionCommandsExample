package ua.widelab.main_commands.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.widelab.compose_components.dimensions
import ua.widelab.main_commands.entities.CommandResult
import ua.widelab.main_commands.presentation.MainCommandsViewModel

@Composable
internal fun CommandsScreen(
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
private fun CommandsScreen(
    isRecording: Boolean,
    commands: List<CommandResult>,
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = MaterialTheme.dimensions.screenPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(reverseLayout = true) {

            items(commands) {
                Text(text = it.toString())
            }
        }
        Column(
            modifier = Modifier
                .weight(1f, false)
                .align(Alignment.CenterHorizontally)
        ) {
            RecordingButton(
                isRecording = isRecording
            ) {
                when (isRecording) {
                    true -> stopRecording()
                    false -> startRecording()
                }
            }
        }

        DisposableEffect(Unit) {
            startRecording()
            onDispose {
                stopRecording()
            }
        }
    }
    /*
    not recording:
        push button and command - highlight
        padding
        commands (all not active)
    recording:
        no current:
            desc of all commands and highlight
            padding
            commands
        has current:
            current
            commands


       IF no value and not active- NO VALUE
       IF no value and active - show animation?
       IF value and active - show animation?

       animation - probably I need some ID for each command, so it animates correctly
     */
}

@Composable
private fun RecordingButton(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    onClick: () -> Unit
) {
    val scaleAnimation = remember { Animatable(1.5f) }
    val scale by scaleAnimation.asState()
    LargeFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.scale(scale),
            painter = painterResource(id = R.drawable.mic),
            contentDescription = null
        )
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            scaleAnimation.animateTo(
                targetValue = 2f,
                animationSpec = infiniteRepeatable(
                    tween(500),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            scaleAnimation.animateTo(1.5f)
        }
    }
}