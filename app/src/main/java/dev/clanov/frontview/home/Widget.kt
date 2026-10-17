package dev.clanov.frontview.home

enum class Widget(val label: String, val note: String) {
    CLOCK("시계", "아날로그 · 세계 시계"),
    WEATHER("날씨", "시간별 · 주간"),
    CALENDAR("캘린더", "월간 · 오늘 일정"),
    TASKS("할 일", "목록 · 오늘"),
    MUSIC("Apple Music", "재생 중"),
    GEMINI("Gemini", "앱 열기"),
    GMAIL("Gmail", "받은편지함"),
    INSTAGRAM("Instagram DM", "새 메시지"),
    NEWS("Google 뉴스", "주제별"),
    DELIVERY("배송조회", "택배 추적"),
    KBO("야구 모니터", "실시간 스코어"),
    EMPTY("빈 칸", "비워 두기");

    /** Gemini opens its own app and an empty slot has nothing to show. */
    val hasFullScreen: Boolean get() = this != GEMINI && this != EMPTY
}
