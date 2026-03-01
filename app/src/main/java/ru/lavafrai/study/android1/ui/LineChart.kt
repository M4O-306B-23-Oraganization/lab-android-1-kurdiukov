package ru.lavafrai.study.android1.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun LineChart(
    data: FloatArray,
    label: String,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF4EEAB0),
) {
    if (data.isEmpty()) return

    val maxVal = remember(data) { data.max().coerceAtLeast(1f) }
    val minVal = remember(data) { data.min() }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp, start = 48.dp)
        ) {
            Canvas(modifier = Modifier.size(10.dp)) {
                drawCircle(color = lineColor, radius = size.minDimension / 2)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
            )
        }

        val density = LocalDensity.current
        val textPaint = remember {
            Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = with(density) { 11.sp.toPx() }
                color = Color.White.toArgb()
                textAlign = android.graphics.Paint.Align.RIGHT
            }
        }

        val yLabels = remember(minVal, maxVal) {
            computeYLabels(minVal, maxVal)
        }
        val yMin = remember(yLabels) { yLabels.first() }
        val yMax = remember(yLabels) { yLabels.last() }

        Row(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFF1A1A2E))
            ) {
                val leftPadding = 60f
                val rightPadding = 16f
                val topPadding = 16f
                val bottomPadding = 16f

                val chartWidth = size.width - leftPadding - rightPadding
                val chartHeight = size.height - topPadding - bottomPadding

                val yRange = yMax - yMin
                if (yRange == 0f) return@Canvas

                val gridColor = Color(0xFF2A3A4A)
                for (yLabel in yLabels) {
                    val y = topPadding + chartHeight * (1f - (yLabel - yMin) / yRange)
                    // Grid line
                    drawLine(
                        color = gridColor,
                        start = Offset(leftPadding, y),
                        end = Offset(size.width - rightPadding, y),
                        strokeWidth = 1f,
                    )
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            formatLabel(yLabel),
                            leftPadding - 8f,
                            y + textPaint.textSize / 3f,
                            textPaint,
                        )
                    }
                }

                drawSignalLine(
                    data = data,
                    color = lineColor,
                    leftPadding = leftPadding,
                    topPadding = topPadding,
                    chartWidth = chartWidth,
                    chartHeight = chartHeight,
                    yMin = yMin,
                    yRange = yRange,
                )
            }
        }
    }
}

private fun DrawScope.drawSignalLine(
    data: FloatArray,
    color: Color,
    leftPadding: Float,
    topPadding: Float,
    chartWidth: Float,
    chartHeight: Float,
    yMin: Float,
    yRange: Float,
) {
    if (data.size < 2) return

    val path = Path()
    val stepX = chartWidth / (data.size - 1).toFloat()

    for (i in data.indices) {
        val x = leftPadding + i * stepX
        val y = topPadding + chartHeight * (1f - (data[i] - yMin) / yRange)
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = 2f),
    )
}

private fun computeYLabels(minVal: Float, maxVal: Float): List<Float> {
    val range = maxVal - minVal
    if (range == 0f) return listOf(minVal)

    val count = 5
    val step = range / (count - 1)
    return (0 until count).map { i ->
        val value = minVal + i * step
        (Math.round(value * 10f) / 10f)
    }
}

private fun formatLabel(value: Float): String {
    return if (value == value.toLong().toFloat()) {
        "%.1f".format(value)
    } else {
        "%.1f".format(value)
    }
}
