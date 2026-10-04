package com.example.data.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.random.Random

/**
 * Generates procedural, calming study ambience (Rain sounds, Pink Noise, Alpha Binaural Waves)
 * completely on-device using Android AudioTrack. No heavy audio files needed!
 */
class AmbienceAudioPlayer {

    private var audioTrack: AudioTrack? = null
    private var audioJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var isPlaying: Boolean = false
        private set

    var currentVolume: Float = 0.5f
        private set

    var currentMode: String = "RAIN" // RAIN, PINK_NOISE, DEEP_BINAURAL
        private set

    fun start(mode: String = "RAIN", volume: Float = 0.5f) {
        stop()
        currentMode = mode
        currentVolume = volume
        isPlaying = true

        audioJob = scope.launch {
            val sampleRate = 44100
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = minBufferSize * 2

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack = track
            track.setVolume(currentVolume)
            track.play()

            val buffer = ShortArray(bufferSize / 2)
            var phase = 0.0
            val rnd = Random(System.currentTimeMillis())
            var b0 = 0.0; var b1 = 0.0; var b2 = 0.0; var b3 = 0.0; var b4 = 0.0; var b5 = 0.0; var b6 = 0.0

            try {
                while (isActive && isPlaying) {
                    when (currentMode) {
                        "RAIN" -> {
                            // Rain simulation: pink noise with random droplet bursts
                            for (i in buffer.indices) {
                                val white = rnd.nextDouble(-1.0, 1.0)
                                b0 = 0.99886 * b0 + white * 0.0555179
                                b1 = 0.99332 * b1 + white * 0.0750759
                                b2 = 0.96900 * b2 + white * 0.1538520
                                b3 = 0.86650 * b3 + white * 0.3104856
                                b4 = 0.55000 * b4 + white * 0.5329522
                                b5 = -0.7616 * b5 - white * 0.0168980
                                val pink = (b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362) * 0.11
                                b6 = white * 0.115926
                                
                                // Random soft raindrop impact
                                val drop = if (rnd.nextInt(250) == 0) rnd.nextDouble(0.2, 0.6) else 0.0
                                val mixed = ((pink * 0.7) + drop).coerceIn(-1.0, 1.0)
                                buffer[i] = (mixed * 8000).toInt().toShort()
                            }
                        }
                        "PINK_NOISE" -> {
                            // Smooth relaxing pink noise
                            for (i in buffer.indices) {
                                val white = rnd.nextDouble(-1.0, 1.0)
                                b0 = 0.99886 * b0 + white * 0.0555179
                                b1 = 0.99332 * b1 + white * 0.0750759
                                b2 = 0.96900 * b2 + white * 0.1538520
                                b3 = 0.86650 * b3 + white * 0.3104856
                                b4 = 0.55000 * b4 + white * 0.5329522
                                b5 = -0.7616 * b5 - white * 0.0168980
                                val pink = (b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362) * 0.11
                                b6 = white * 0.115926
                                buffer[i] = (pink * 9000).coerceIn(-32767.0, 32767.0).toInt().toShort()
                            }
                        }
                        else -> {
                            // Deep Binaural Study Tone (432Hz calming pure sine with subtle alpha harmonic)
                            val freq = 432.0
                            val alphaBeat = 10.0 // 10Hz alpha wave
                            for (i in buffer.indices) {
                                val wave = sin(phase) * 0.8 + sin(phase * (alphaBeat / freq + 1.0)) * 0.2
                                buffer[i] = (wave * 6000).toInt().toShort()
                                phase += (2.0 * Math.PI * freq) / sampleRate
                                if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                            }
                        }
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (e: Exception) {
                // Audio generation ended
            }
        }
    }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        try {
            audioTrack?.setVolume(currentVolume)
        } catch (_: Exception) {}
    }

    fun stop() {
        isPlaying = false
        audioJob?.cancel()
        audioJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }
}
