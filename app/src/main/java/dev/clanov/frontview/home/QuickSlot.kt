package dev.clanov.frontview.home

enum class QuickSlot(val label: String) {
    MUSE("Muse"),
    GMAIL("Gmail"),
    INSTAGRAM("Instagram DM"),
    NEWS("Google News"),
    DELIVERY("Delivery"),
    BASEBALL("KBO")
}

data class QuickSlots(
    val first: QuickSlot = QuickSlot.MUSE,
    val second: QuickSlot = QuickSlot.GMAIL
) {
    init {
        require(first != second)
    }
}

fun nextSecondary(current: QuickSlot): QuickSlot {
    val options = QuickSlot.entries.filterNot { it == QuickSlot.MUSE }
    return options[(options.indexOf(current) + 1).mod(options.size)]
}
