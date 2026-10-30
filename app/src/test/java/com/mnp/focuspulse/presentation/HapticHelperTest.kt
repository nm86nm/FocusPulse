package com.mnp.focuspulse.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class HapticHelperTest {

    private lateinit var context: Context
    private lateinit var vibrator: Vibrator

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        vibrator = mockk(relaxed = true)

        every { vibrator.hasVibrator() } returns true
        every { context.getSystemService(Context.VIBRATOR_SERVICE) } returns vibrator

        mockkStatic(VibrationEffect::class)
        every { VibrationEffect.createWaveform(any(), any(), any()) } returns mockk()
        every { VibrationEffect.createOneShot(any(), any()) } returns mockk()
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun testPulseInhale() {
        val helper = HapticHelper(context)
        helper.pulseInhale()

        verify { vibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun testPulseExhale() {
        val helper = HapticHelper(context)
        helper.pulseExhale()

        verify { vibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun testPulsePhaseChange() {
        val helper = HapticHelper(context)
        helper.pulsePhaseChange()

        verify { vibrator.vibrate(any<VibrationEffect>()) }
    }
}
