package ua.widelab.main_commands.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
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
                val value = when (it.isCurrent) {
                    true -> {
                        val ellipsisAnimatable = remember { Animatable(0f) }
                        val ellipsisCount by ellipsisAnimatable.asState()
                        LaunchedEffect(Unit) {
                            ellipsisAnimatable.animateTo(
                                targetValue = 4f,
                                animationSpec = infiniteRepeatable(
                                    tween(durationMillis = 1000),
                                    repeatMode = RepeatMode.Restart
                                )
                            )
                        }
                        it.value + ".".repeat(ellipsisCount.toInt())
                    }

                    false -> it.value.ifEmpty { stringResource(id = R.string.empty_value) }
                }
                Item(
                    isHighlighted = it.isCurrent,
                    title = it.name,
                    value = value
                )
            }
            if (!isRecording) {
                item {
                    Item(
                        text = stringResource(id = R.string.no_recording_hint) + System.lineSeparator() + stringResource(
                            id = R.string.recording_hint
                        )
                    )
                }
            } else if (commands.lastOrNull()?.isCurrent != true) {
                item {
                    Item(
                        text = stringResource(id = R.string.recording_hint)
                    )
                }
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

@Composable
private fun Item(
    isHighlighted: Boolean,
    extraPadding: Boolean,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (extraPadding) MaterialTheme.dimensions.screenPadding else MaterialTheme.dimensions.cardPadding),
        colors = if (isHighlighted) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer) else CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(all = MaterialTheme.dimensions.innerCardPadding)) {
            content()
        }
    }
}

@Composable
private fun Item(
    text: String
) {
    Item(
        isHighlighted = true,
        extraPadding = true
    ) {
        Text(text = text)
    }
}

@Composable
private fun Item(
    title: String,
    value: String,
    isHighlighted: Boolean
) {
    Item(
        isHighlighted = isHighlighted,
        extraPadding = false
    ) {
        Column {
            Text(text = title)
            Text(text = value)
        }
    }
}