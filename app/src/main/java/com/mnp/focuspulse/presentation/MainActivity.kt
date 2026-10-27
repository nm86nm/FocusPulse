/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.mnp.focuspulse.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import kotlinx.coroutines.delay

enum class BreathPhase(val title: String) {
    INHALE("Inhale..."),
    HOLD_IN("Hold..."),
    EXHALE("Exhale..."),
    HOLD_OUT("Rest...")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FocusPulseApp()
        }
    }
}

@Composable
fun FocusPulseApp() {
    MaterialTheme {
        Scaffold(
            timeText = { TimeText() },
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
        ) {
            FocusSessionScreen()
        }
    }
}

@Composable
fun FocusSessionScreen() {
    val context = LocalContext.current
    val hapticHelper = remember { HapticHelper(context) }

    var isRunning by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf(BreathPhase.INHALE) }
    var timerSeconds by remember { mutableIntStateOf(4) }
    var simulatedHeartRate by remember { mutableIntStateOf(72) }

    // Dynamic UI Scale to represent lung expansion
    val animatedScale by animateFloatAsState(
        targetValue = when (phase) {
            BreathPhase.INHALE -> 1.3f
            BreathPhase.HOLD_IN -> 1.3f
            BreathPhase.EXHALE -> 0.8f
            BreathPhase.HOLD_OUT -> 0.8f
        },
        animationSpec = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
        label = "LungScaleAnimation"
    )

    // Breathing Cycle Loop
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                // INHALE
                phase = BreathPhase.INHALE
                hapticHelper.pulseInhale()
                for (i in 4 downTo 1) {
                    timerSeconds = i
                    delay(1000)
                }

                // HOLD IN
                phase = BreathPhase.HOLD_IN
                hapticHelper.pulsePhaseChange()
                for (i in 4 downTo 1) {
                    timerSeconds = i
                    delay(1000)
                }

                // EXHALE
                phase = BreathPhase.EXHALE
                hapticHelper.pulseExhale()
                for (i in 4 downTo 1) {
                    timerSeconds = i
                    delay(1000)
                }

                // HOLD OUT
                phase = BreathPhase.HOLD_OUT
                hapticHelper.pulsePhaseChange()
                for (i in 4 downTo 1) {
                    timerSeconds = i
                    delay(1000)
                }

                // Simulate slight heart rate drop during box breathing
                if (simulatedHeartRate > 62) simulatedHeartRate -= 1
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Outer Progress / Heart Rate Visualizer
        CircularProgressIndicator(
            progress = if (isRunning) (4 - timerSeconds) / 4f else 0f,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            indicatorColor = when (phase) {
                BreathPhase.INHALE -> Color(0xFF00E676)
                BreathPhase.HOLD_IN -> Color(0xFFFFD600)
                BreathPhase.EXHALE -> Color(0xFF2979FF)
                BreathPhase.HOLD_OUT -> Color(0xFFAA00FF)
            },
            trackColor = Color.DarkGray.copy(alpha = 0.3f),
            strokeWidth = 4.dp
        )

        // Center Animated Core
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(if (isRunning) animatedScale else 1.0f)
                .padding(16.dp)
        ) {
            if (isRunning) {
                Text(
                    text = phase.title,
                    style = MaterialTheme.typography.title3,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$timerSeconds",
                    style = MaterialTheme.typography.display1,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "♥ $simulatedHeartRate BPM",
                    style = MaterialTheme.typography.caption2,
                    color = Color(0xFFFF5252)
                )
            } else {
                Text(
                    text = "FocusPulse",
                    style = MaterialTheme.typography.title2,
                    color = Color(0xFF00E676),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Box Breathing",
                    style = MaterialTheme.typography.caption1,
                    color = Color.Gray
                )
            }
        }

        // Action Button Pinning at Bottom
        CompactButton(
            onClick = {
                isRunning = !isRunning
                if (!isRunning) {
                    simulatedHeartRate = 72
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isRunning) Color(0xFFFF5252) else Color(0xFF00E676)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = if (isRunning) "Stop" else "Start",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}