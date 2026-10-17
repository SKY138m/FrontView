package dev.clanov.frontview.home

import dev.clanov.frontview.ui.SlotSize

data class HomeLayout(val slots: List<Widget> = DEFAULT_SLOTS) {
    init {
        require(slots.size == SLOT_COUNT)
    }

    /** Puts [widget] in [index]. A widget already shown elsewhere trades places with the old one. */
    fun place(index: Int, widget: Widget): HomeLayout {
        val next = slots.toMutableList()
        val previous = next[index]
        if (widget != Widget.EMPTY) {
            val other = next.indexOf(widget)
            if (other >= 0 && other != index) next[other] = previous
        }
        next[index] = widget
        return HomeLayout(next)
    }

    fun serialize(): String = slots.joinToString(",") { it.name }

    companion object {
        const val SLOT_COUNT = 7

        val SIZES = listOf(
            SlotSize.L, SlotSize.L, SlotSize.M, SlotSize.M,
            SlotSize.L, SlotSize.L, SlotSize.XL
        )

        /** Top-left corner of every slot in design pixels. */
        val POSITIONS = listOf(
            28 to 32, 408 to 32, 28 to 384, 408 to 384,
            28 to 560, 408 to 560, 28 to 912
        )

        val DEFAULT_SLOTS = listOf(
            Widget.CLOCK, Widget.WEATHER, Widget.GEMINI, Widget.GMAIL,
            Widget.TASKS, Widget.CALENDAR, Widget.MUSIC
        )

        fun parse(text: String?): HomeLayout {
            val parsed = text?.split(",")?.map { name -> Widget.entries.firstOrNull { it.name == name } }
            if (parsed == null || parsed.size != SLOT_COUNT || parsed.any { it == null }) return HomeLayout()
            val widgets = parsed.filterNotNull()
            val shown = widgets.filter { it != Widget.EMPTY }
            return if (shown.size == shown.toSet().size) HomeLayout(widgets) else HomeLayout()
        }
    }
}
