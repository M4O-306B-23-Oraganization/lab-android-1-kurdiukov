package ru.lavafrai.study.android1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ru.lavafrai.study.android1.navigation.PreviewDestination

class MainViewModel(
    val navController: NavController,
): ViewModel() {
    fun openSignalPreview(
        amplitude: Float,
        phase: Float,
        frequency: Float,
        samples: Int,
        samplingPeriodUs: Float,
    ) {
        navController.navigate(PreviewDestination(
            amplitude = amplitude,
            frequency = frequency,
            phase = phase,
            samples = samples,
            samplingPeriodUs = samplingPeriodUs,
        ))
    }
}