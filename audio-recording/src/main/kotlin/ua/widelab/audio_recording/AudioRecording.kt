package ua.widelab.audio_recording

import kotlinx.coroutines.flow.Flow

public interface AudioRecording {
    fun start()
    fun stop()

    val results: Flow<String>

    val isListening: Flow<Boolean>
}