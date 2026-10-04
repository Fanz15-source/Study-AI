package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun ConfettiOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    val transition = rememberInfiniteTransition(label = "confetti")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProg"
    )

    val particles = remember {
        val colors = listOf(SoftLavender, ElectricBlue, MintAccent, GoldStar, Color(0xFFFF6584))
        List(50) {
            ConfettiParticle(
                xNorm = Random.nextFloat(),
                yOffsetNorm = Random.nextFloat() * 0.4f,
                speed = Random.nextFloat() * 0.6f + 0.7f,
                size = Random.nextFloat() * 8f + 6f,
                color = colors[Random.nextInt(colors.size)],
                rotationSpeed = Random.nextFloat() * 720f - 360f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val x = p.xNorm * size.width
            val y = ((p.yOffsetNorm + (progress * p.speed)) % 1f) * size.height
            val currentRotation = progress * p.rotationSpeed

            rotate(degrees = currentRotation, pivot = Offset(x, y)) {
                drawRect(
                    color = p.color.copy(alpha = 0.85f),
                    topLeft = Offset(x, y),
                    size = Size(p.size, p.size * 0.55f)
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val xNorm: Float,
    val yOffsetNorm: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float
)
