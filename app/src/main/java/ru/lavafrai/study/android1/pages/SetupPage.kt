package ru.lavafrai.study.android1.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import ru.lavafrai.study.android1.models.SignalPhase


@Composable
fun SetupPage(
    openSignalPreview: (amplitude: Float, frequency: Float, phase: SignalPhase, samples: Int, samplingPeriodUs: Float) -> Unit,
) {
    // val phasePickerState = rememberPhasePickerState(initialAngle = 0f)
    var signalPhase by remember { mutableStateOf(SignalPhase(0f)) }
    var signalAmplitude by remember { mutableFloatStateOf(12f) }
    var signalFrequency by remember { mutableFloatStateOf(100f) } // in KHz
    var signalSamples by remember { mutableIntStateOf(1024) }
    var signalSamplingPeriodUs by remember { mutableFloatStateOf(0.05f) } // in μs
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text(text = "Настройка сигнала") }, scrollBehavior = scrollBehavior)
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            SignalFrequencyPicker(
                value = signalFrequency,
                onValueChange = { signalFrequency = it },
            )
            SignalAmplitudePicker(
                value = signalAmplitude,
                onValueChange = { signalAmplitude = it },
            )
            SignalPhasePicker(
                value = signalPhase,
                onValueChange = { signalPhase = it },
            )
            SignalSamplesPicker(
                value = signalSamples,
                onValueChange = { signalSamples = it },
            )
            SignalSamplingPeriodPicker(
                value = signalSamplingPeriodUs,
                onValueChange = { signalSamplingPeriodUs = it },
            )
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = { openSignalPreview(signalAmplitude, signalFrequency * 1000, signalPhase, signalSamples, signalSamplingPeriodUs) },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text("Просмотр сигнала")
            }
        }
    }
}

@Composable
fun SignalFrequencyPicker(
    value: Float,
    onValueChange: (Float) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Частота сигнала", style = MaterialTheme.typography.headlineSmall)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 10f..1000f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Text("Выбранное значение: ${"%.1f".format(value)} КГц")
    }
}

@Composable
fun SignalAmplitudePicker(
    value: Float,
    onValueChange: (Float) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Амплитуда сигнала", style = MaterialTheme.typography.headlineSmall)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Text("Выбранное значение: ${"%.1f".format(value)} В")
    }
}

@Composable
fun SignalPhasePicker(
    value: SignalPhase,
    onValueChange: (SignalPhase) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Фаза сигнала", style = MaterialTheme.typography.headlineSmall)

        val sliderState = rememberSliderState(
            value = value.degrees,
            valueRange = -180f..180f,
        )
        LaunchedEffect(sliderState.value) {
            onValueChange(SignalPhase(sliderState.value))
        }
        Slider(
            state = sliderState,
            track = { SliderDefaults.CenteredTrack(sliderState = sliderState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Выбранное значение: ${"%.1f".format(sliderState.value)}°",
            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 16.dp) {
                IconButton(
                    shapes = IconButtonDefaults.shapes(),
                    onClick = { sliderState.value = 0f },
                    colors = IconButtonDefaults.filledIconButtonColors()
                ) {
                    Icon(Icons.Rounded.SettingsBackupRestore, "revert default value")
                }
            }
        }
    }
}

@Composable
fun SignalSamplesPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    val sampleOptions = listOf(64, 128, 256, 512, 1024, 2048, 4096)
    val currentIndex = sampleOptions.indexOf(value).coerceAtLeast(0).toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text("Количество семплов", style = MaterialTheme.typography.headlineSmall)
            Slider(
                value = currentIndex,
                onValueChange = { idx ->
                    val roundedIndex = idx.roundToInt().coerceIn(0, sampleOptions.size - 1)
                    onValueChange(sampleOptions[roundedIndex])
                },
                valueRange = 0f..(sampleOptions.size - 1).toFloat(),
                steps = sampleOptions.size - 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Text("Выбранное значение: $value")
        }
    }
}

@Composable
fun SignalSamplingPeriodPicker(
    value: Float,
    onValueChange: (Float) -> Unit,
) = Card(
    modifier = Modifier.fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Период семплирования", style = MaterialTheme.typography.headlineSmall)
        // Range: 0.01 μs to 5.0 μs — covers all meaningful frequencies from 10 KHz to 1 MHz
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.01f..5.0f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        val totalWindowUs = value * 1024
        Text("Выбранное значение: ${"%.3f".format(value)} мкс  (окно ~${"%.1f".format(totalWindowUs)} мкс)")
    }
}
