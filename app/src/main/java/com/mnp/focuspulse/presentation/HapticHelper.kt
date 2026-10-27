package com.mnp.focuspulse.presentation

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class HapticHelper(context: Context) {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun pulseInhale() {
        val pattern = longArrayOf(0, 100, 100, 200, 100, 300)
        val amplitudes = intArrayOf(0, 50, 0, 150, 0, 255)
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        }
    }

    fun pulseExhale() {
        val pattern = longArrayOf(0, 300, 100, 200, 100, 100)
        val amplitudes = intArrayOf(0, 255, 0, 150, 0, 50)
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        }
    }

    fun pulsePhaseChange() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}