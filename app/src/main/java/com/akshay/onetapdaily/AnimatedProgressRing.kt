package com.akshay.onetapdaily

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedProgressRing(
    percentage: Int,
    size: Dp = 120.dp
) {

    val animatedProgress = animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(1000),
        label = ""
    )

    val ringColor =
        when {
            percentage == 100 -> Color(0xFF039108) // Green
            percentage >= 80 -> Color(0xFF1F6B1F)
            percentage >= 50 -> Color(0xFFFFC107) // Yellow/Amber
            percentage >= 30 -> Color(0xFFFF9800)
            percentage > 0 -> Color(0xFF930805)   // Red

            else -> Color.Gray
        }

    Box(
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(size)
        ) {

            drawArc(
                color = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 12f,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = animatedProgress.value * 360f,
                useCenter = false,
                style = Stroke(
                    width = 12f,
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}