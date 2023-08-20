package ua.widelab.audio_recording.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.widelab.audio_recording.*
@Module
@InstallIn(SingletonComponent::class)
abstract class AudioRecordingModule {
    @Binds
    internal abstract fun bindAudioRecorder(
        tensorFlowAudioRecording : TensorFlowAudioRecorder
    ): AudioRecorder
}