package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun AmbienceBackground(
    ambience: String,
    isDark: Boolean,
    animationsEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (ambience) {
            "COZY_LIBRARY" -> {
                Image(
                    painter = painterResource(id = R.drawable.bg_study_hero),
                    contentDescription = "Cozy Library Ambience",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = if (isDark) listOf(
                                    DeepNavy.copy(alpha = 0.88f),
                                    MidnightBlue.copy(alpha = 0.94f)
                                ) else listOf(
                                    WarmCream.copy(alpha = 0.85f),
                                    WarmCream.copy(alpha = 0.95f)
                                )
                            )
                        )
                )
            }
            "RAINY_WINDOW" -> {
                Image(
                    painter = painterResource(id = R.drawable.bg_rainy_ambience),
                    contentDescription = "Rainy Window Ambience",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = if (isDark) listOf(
                                    DeepNavy.copy(alpha = 0.82f),
                                    Color(0xFF0F172A).copy(alpha = 0.94f)
                                ) else listOf(
                                    Color(0xFFE2E8F0).copy(alpha = 0.85f),
                                    WarmCream.copy(alpha = 0.96f)
                                )
                            )
                        )
                )
                if (animationsEnabled) {
                    RainDropsCanvas(isDark = isDark)
                }
            }
            "NIGHT_SKY" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1B2244),
                                    Color(0xFF0C101F),
                                    Color(0xFF070A14)
                                ),
                                center = Offset(300f, 200f),
                                radius = 900f
                            )
                        )
                )
                if (animationsEnabled) {
                    TwinklingStarsCanvas()
                }
            }
            "NATURE_CALM" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = if (isDark) listOf(
                                    Color(0xFF0D2322),
                                    DeepNavy,
                                    Color(0xFF132A26)
                                ) else listOf(
                                    Color(0xFFEBF7F2),
                                    WarmCream,
                                    Color(0xFFE0F2E9)
                                )
                            )
                        )
                )
            }
            "MINIMAL_WORKSPACE" -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isDark) DeepNavy else WarmCream
                        )
                )
            }
            else -> { // "CALM_GRADIENT"
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = if (isDark) listOf(
                                    DeepNavy,
                                    MidnightBlue,
                                    Color(0xFF1A1F3C)
                                ) else listOf(
                                    Color(0xFFFAF7F2),
                                    WarmCream,
                                    Color(0xFFF0EAE1)
                                )
                            )
                        )
                )
                if (animationsEnabled && isDark) {
                    AmbientGlowCanvas()
                }
            }
        }

        // Main app content layered above ambience
        content()
    }
}

@Composable
fun RainDropsCanvas(isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "rain")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rainPhase"
    )

    val drops = remember {
        List(35) {
            Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat() * 20f + 15f)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val dropColor = if (isDark) SoftLavender.copy(alpha = 0.25f) else ElectricBlue.copy(alpha = 0.2f)
        drops.forEach { (normX, normStartY, length) ->
            val x = normX * size.width
            val currentY = ((normStartY + phase) % 1f) * (size.height + 100f) - 50f
            drawLine(
                color = dropColor,
                start = Offset(x, currentY),
                end = Offset(x - 3f, currentY + length),
                strokeWidth = 1.8f
            )
        }
    }
}

@Composable
fun TwinklingStarsCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "starsTwinkle"
    )

    val stars = remember {
        List(40) {
            Pair(Random.nextFloat(), Random.nextFloat())
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEachIndexed { i, (normX, normY) ->
            val x = normX * size.width
            val y = normY * size.height
            val starAlpha = if (i % 2 == 0) alphaAnim else (1f - alphaAnim * 0.7f).coerceIn(0.2f, 0.9f)
            drawCircle(
                color = SoftWhite.copy(alpha = starAlpha),
                radius = if (i % 3 == 0) 2.2f else 1.5f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun AmbientGlowCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientGlowScale"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    SoftLavender.copy(alpha = 0.08f),
                    ElectricBlue.copy(alpha = 0.04f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.85f, size.height * 0.15f),
                radius = 320f * scale
            ),
            radius = 320f * scale,
            center = Offset(size.width * 0.85f, size.height * 0.15f)
        )
    }
}
