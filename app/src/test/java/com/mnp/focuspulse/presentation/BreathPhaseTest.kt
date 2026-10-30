package com.mnp.focuspulse.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class BreathPhaseTest {

    @Test
    fun testBreathPhaseTitles() {
        assertEquals("Inhale...", BreathPhase.INHALE.title)
        assertEquals("Hold...", BreathPhase.HOLD_IN.title)
        assertEquals("Exhale...", BreathPhase.EXHALE.title)
        assertEquals("Rest...", BreathPhase.HOLD_OUT.title)
    }

    @Test
    fun testBreathPhaseValuesCount() {
        val values = BreathPhase.values()
        assertEquals(4, values.size)
    }
}
