package ru.lavafrai.study.android1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.lavafrai.study.android1.navigation.util.navTypeOf
import ru.lavafrai.study.android1.pages.PreviewPage
import ru.lavafrai.study.android1.pages.SetupPage
import ru.lavafrai.study.android1.viewmodels.MainViewModel
import kotlin.reflect.typeOf


@Composable
fun AppNavHost(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController,
        startDestination = MainDestination
    ) {
        composable<MainDestination> {
            SetupPage(
                openSignalPreview = { amplitude, frequency, phase, samples, samplingPeriodUs ->
                    viewModel.openSignalPreview(
                        amplitude = amplitude,
                        frequency = frequency,
                        phase = phase.degrees,
                        samples = samples,
                        samplingPeriodUs = samplingPeriodUs,
                    )
                }
            )
        }

        composable<PreviewDestination>(
            typeMap = mapOf(
                typeOf<Double>() to navTypeOf<Double>(),
                typeOf<Float>() to navTypeOf<Float>(),
            )
        ) { backStackEntry ->
            val dest = backStackEntry.toRoute<PreviewDestination>()
            PreviewPage(
                amplitude = dest.amplitude,
                frequency = dest.frequency,
                phase = dest.phase,
                samples = dest.samples,
                samplingPeriodUs = dest.samplingPeriodUs,
                onBack = { navController.popBackStack() },
            )
        }
    }
}