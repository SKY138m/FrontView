package dev.clanov.frontview.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeLayoutTest {
    @Test fun defaultLayout() {
        val layout = HomeLayout()
        assertEquals(7, layout.slots.size)
        assertEquals(Widget.CLOCK, layout.slots[0])
        assertEquals(Widget.MUSIC, layout.slots[6])
    }

    @Test fun placingShownWidgetSwaps() {
        val layout = HomeLayout().place(0, Widget.MUSIC)
        assertEquals(Widget.MUSIC, layout.slots[0])
        assertEquals(Widget.CLOCK, layout.slots[6])
    }

    @Test fun placingNewWidgetReplaces() {
        val layout = HomeLayout().place(2, Widget.NEWS)
        assertEquals(Widget.NEWS, layout.slots[2])
        assertEquals(1, layout.slots.count { it == Widget.NEWS })
    }

    @Test fun emptyMayRepeat() {
        val layout = HomeLayout().place(0, Widget.EMPTY).place(1, Widget.EMPTY)
        assertEquals(2, layout.slots.count { it == Widget.EMPTY })
    }

    @Test fun roundTrip() {
        val layout = HomeLayout().place(3, Widget.KBO).place(4, Widget.EMPTY)
        assertEquals(layout, HomeLayout.parse(layout.serialize()))
    }

    @Test fun badInputFallsBack() {
        assertEquals(HomeLayout(), HomeLayout.parse(null))
        assertEquals(HomeLayout(), HomeLayout.parse("CLOCK,CLOCK"))
        assertEquals(HomeLayout(), HomeLayout.parse("A,B,C,D,E,F,G"))
        assertTrue(HomeLayout.parse("CLOCK,CLOCK,CLOCK,CLOCK,CLOCK,CLOCK,CLOCK") == HomeLayout())
    }
}
