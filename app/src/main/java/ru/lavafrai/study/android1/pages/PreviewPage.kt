package ru.lavafrai.study.android1.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ru.lavafrai.study.android1.ui.LineChart
import ru.lavafrai.study.android1.utils.computeSpectrum
import ru.lavafrai.study.android1.utils.generateSineSignal

@Composable
fun PreviewPage(
    amplitude: Float,
    frequency: Float,
    phase: Float,
    samples: Int,
    samplingPeriodUs: Float,
    onBack: () -> Unit,
) {
    val signalData = remember(amplitude, frequency, phase, samples, samplingPeriodUs) {
        generateSineSignal(
            amplitude = amplitude,
            frequency = frequency,
            phase = phase,
            samples = samples,
            samplingPeriodUs = samplingPeriodUs,
        )
    }

    val spectrumData = remember(signalData) {
        computeSpectrum(signalData)
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Настройка сигнала") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        if (isLandscape) {
            // Landscape: Signal and Spectrum side by side
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                LineChart(
                    data = signalData,
                    label = "Signal",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                LineChart(
                    data = spectrumData,
                    label = "Spectrum",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        } else {
            // Portrait: Signal and Spectrum stacked
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                LineChart(
                    data = signalData,
                    label = "Signal",
                    modifier = Modifier.fillMaxWidth(),
                )
                LineChart(
                    data = spectrumData,
                    label = "Spectrum",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
