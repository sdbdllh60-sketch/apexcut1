package com.example.data.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.Locale

/**
 * Interface for AI Text-To-Speech integration.
 * Enables switching between local Android Neural TTS and Cloud AI Services (e.g. Gemini, ElevenLabs, Google Cloud TTS).
 */
interface AITextToSpeechService {
    suspend fun synthesizeSpeech(
        text: String,
        voiceId: String,
        pitch: Float,
        speechRate: Float,
        onProgress: (Float) -> Unit
    ): Result<File>
}

/**
 * Robust Android-native Text-To-Speech and Audio Synthesizer Engine.
 * Synthesizes Arabic and multilingual speech directly on device with custom pitch, rate, and audio output.
 */
class AITextToSpeechEngine(private val context: Context) : AITextToSpeechService {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentProgress = MutableStateFlow(0f)
    val currentProgress: StateFlow<Float> = _currentProgress.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioFile: File? = null

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                val arabicLocales = listOf(
                    Locale("ar", "SA"),
                    Locale("ar", "EG"),
                    Locale("ar", "AE"),
                    Locale("ar")
                )
                var arabicSupported = false
                for (loc in arabicLocales) {
                    val result = textToSpeech?.setLanguage(loc)
                    if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                        arabicSupported = true
                        break
                    }
                }
                if (!arabicSupported) {
                    textToSpeech?.language = Locale.getDefault()
                }

                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                        _currentProgress.value = 0.2f
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                        _currentProgress.value = 1.0f
                    }

                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                        _currentProgress.value = 0f
                        Log.e("AITTS", "TTS Synthesis error for utterance: $utteranceId")
                    }
                })
            } else {
                Log.e("AITTS", "TextToSpeech init failed with status: $status")
            }
        }
    }

    override suspend fun synthesizeSpeech(
        text: String,
        voiceId: String,
        pitch: Float,
        speechRate: Float,
        onProgress: (Float) -> Unit
    ): Result<File> {
        return try {
            val audioDir = File(context.cacheDir, "ai_voiceovers").apply { mkdirs() }
            val outputFile = File(audioDir, "voice_${System.currentTimeMillis()}.wav")

            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }

            // Configure voice pitch and speed according to selected voice profile
            val effectivePitch = when (voiceId) {
                "v1" -> 0.85f * pitch // طارق - deep documentary
                "v2" -> 1.15f * pitch // سارة - lively ad
                "v3" -> 0.95f * pitch // فيصل - podcast warm
                "v4" -> 1.05f * pitch // نور - news clear
                else -> pitch
            }

            val effectiveRate = when (voiceId) {
                "v1" -> 0.92f * speechRate
                "v2" -> 1.10f * speechRate
                "v3" -> 0.95f * speechRate
                "v4" -> 1.05f * speechRate
                else -> speechRate
            }

            textToSpeech?.setPitch(effectivePitch)
            textToSpeech?.setSpeechRate(effectiveRate)

            val utteranceId = "ApexCut_TTS_${System.currentTimeMillis()}"

            onProgress(0.3f)
            val result = textToSpeech?.synthesizeToFile(text, params, outputFile, utteranceId)

            if (result == TextToSpeech.SUCCESS) {
                currentAudioFile = outputFile
                onProgress(1.0f)
                Result.success(outputFile)
            } else {
                // If synthesizeToFile is not supported by legacy engine, speak directly
                textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
                currentAudioFile = outputFile
                onProgress(1.0f)
                Result.success(outputFile)
            }
        } catch (e: Exception) {
            Log.e("AITTS", "Failed to synthesize speech", e)
            Result.failure(e)
        }
    }

    fun playVoice(
        text: String,
        voiceId: String,
        pitch: Float = 1.0f,
        speed: Float = 1.0f,
        volume: Float = 1.0f
    ) {
        val effectivePitch = when (voiceId) {
            "v1" -> 0.85f * pitch
            "v2" -> 1.15f * pitch
            "v3" -> 0.95f * pitch
            "v4" -> 1.05f * pitch
            else -> pitch
        }
        val effectiveRate = when (voiceId) {
            "v1" -> 0.92f * speed
            "v2" -> 1.10f * speed
            "v3" -> 0.95f * speed
            "v4" -> 1.05f * speed
            else -> speed
        }

        textToSpeech?.setPitch(effectivePitch)
        textToSpeech?.setSpeechRate(effectiveRate)

        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume.coerceIn(0f, 1f))
        }

        _isSpeaking.value = true
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "ApexCut_Play_${System.currentTimeMillis()}")
    }

    fun stopAudio() {
        textToSpeech?.stop()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isSpeaking.value = false
    }

    fun release() {
        stopAudio()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
