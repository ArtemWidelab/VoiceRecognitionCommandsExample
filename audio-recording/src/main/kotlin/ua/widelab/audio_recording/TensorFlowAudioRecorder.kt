package ua.widelab.audio_recording

import android.content.Context
import android.media.AudioRecord
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.core.BaseOptions
import ua.widelab.logger.Logger
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class TensorFlowAudioRecorder @Inject constructor(
    @ApplicationContext context: Context
) : AudioRecorder {

    companion object {
        const val NUM_THREADS = 1
        const val SCORE_THRESHOLD = 0.8f
        const val MAX_RESULT = 1
        const val OVERLAP = 0.5f
        const val MODEL = "my_speech.tflite"
    }

    private lateinit var classifier: AudioClassifier
    private lateinit var tensorAudio: TensorAudio
    private lateinit var recorder: AudioRecord
    private lateinit var executor: ScheduledThreadPoolExecutor

    private val dataFlow = MutableStateFlow("")
    private val isListeningFlow = MutableStateFlow(false)

    private val classifyRunnable = Runnable {
        classifyAudio()
    }

    init {
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(NUM_THREADS)

        // Configures a set of parameters for the classifier and what results will be returned.
        val options = AudioClassifier.AudioClassifierOptions.builder()
            .setScoreThreshold(SCORE_THRESHOLD)
            .setMaxResults(MAX_RESULT)
            .setBaseOptions(baseOptionsBuilder.build())
            .build()

        try {
            classifier = AudioClassifier.createFromFileAndOptions(context, MODEL, options)
            tensorAudio = classifier.createInputTensorAudio()
            recorder = classifier.createAudioRecord()
        } catch (e: IllegalStateException) {
            Logger.e("AudioClassification failed to init", e)
        }
    }

    override fun start() {
        if (recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
            return
        }
        isListeningFlow.tryEmit(true)
        recorder.startRecording()
        executor = ScheduledThreadPoolExecutor(1)

        val lengthInMilliSeconds = ((classifier.requiredInputBufferSize * 1.0f) /
                classifier.requiredTensorAudioFormat.sampleRate) * 1000

        val interval = (lengthInMilliSeconds * (1 - OVERLAP)).toLong()

        executor.scheduleAtFixedRate(
            classifyRunnable,
            0,
            interval,
            TimeUnit.MILLISECONDS
        )
    }

    override fun stop() {
        recorder.stop()
        executor.shutdownNow()
        isListeningFlow.tryEmit(false)
    }

    private fun classifyAudio() {
        tensorAudio.load(recorder)
        val output = classifier.classify(tensorAudio)
        val value = output.firstOrNull()?.categories?.firstOrNull()?.label ?: return
        dataFlow.tryEmit(value)
    }

    override val results: Flow<String> = dataFlow.filter { it != "background" }
    override val isListening: Flow<Boolean> = isListeningFlow
}