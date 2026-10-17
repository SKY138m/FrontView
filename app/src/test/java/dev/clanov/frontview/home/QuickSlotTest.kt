package dev.clanov.frontview.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class QuickSlotTest {
    @Test fun defaultSlots() {
        val slots = QuickSlots()
        assertEquals(QuickSlot.GEMINI, slots.first)
        assertEquals(QuickSlot.GMAIL, slots.second)
    }

    @Test fun cyclingSkipsGemini() {
        for (slot in QuickSlot.entries.filterNot { it == QuickSlot.GEMINI }) {
            assertNotEquals(QuickSlot.GEMINI, nextSecondary(slot))
        }
    }
}
