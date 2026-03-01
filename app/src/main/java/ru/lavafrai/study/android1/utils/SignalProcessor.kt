package ru.lavafrai.study.android1.utils

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun generateSineSignal(
    amplitude: Float,
    frequency: Float,
    phase: Float,
    samples: Int,
    samplingPeriodUs: Float = 0.05f,
): FloatArray {
    val phaseRad = phase * PI.toFloat() / 180f
    val samplingPeriodSec = samplingPeriodUs * 1e-6f
    val result = FloatArray(samples)
    for (i in 0 until samples) {
        val t = i.toFloat() * samplingPeriodSec
        result[i] = amplitude * sin(2f * PI.toFloat() * frequency * t + phaseRad)
    }
    return result
}

fun computeSpectrum(signal: FloatArray): FloatArray {
    val n = signal.size
    val halfN = n / 2
    val spectrum = FloatArray(halfN)

    for (k in 0 until halfN) {
        var re = 0f
        var im = 0f
        for (i in 0 until n) {
            val angle = 2f * PI.toFloat() * k * i / n
            re += signal[i] * cos(angle)
            im -= signal[i] * sin(angle)
        }
        spectrum[k] = sqrt(re * re + im * im)
    }
    return spectrum
}
