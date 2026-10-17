package dev.clanov.frontview.ui.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.Palette

enum class Sky { Sun, Cloud, Moon }
data class HourForecast(val label: String, val temp: Int, val sky: Sky)
data class DayForecast(val name: String, val sky: Sky, val low: Int, val high: Int)
data class EventRow(val title: String, val time: String, val color: Color)
data class TaskRow(val title: String, val done: Boolean, val id: String = "", val listId: String = "")
data class TaskCount(val name: String, val count: Int, val color: Color)
data class MailRow(val from: String, val time: String, val subject: String, val preview: String, val unread: Boolean)
data class DmRow(val initial: String, val name: String, val time: String, val message: String, val unread: Boolean)
data class NewsRow(val title: String, val source: String, val url: String = "")
data class ParcelRow(val name: String, val status: String, val sub: String, val progress: Int, val delivered: Boolean)
data class TrackStep(val label: String, val place: String, val time: String, val reached: Boolean, val current: Boolean)
data class GameRow(val match: String, val state: String, val live: Boolean)
data class StandingRow(val rank: String, val team: String, val record: String, val pct: String)
data class WeatherDetail(val label: String, val value: String, val unit: String = "")

object SampleData {
    var location by mutableStateOf("서울")
    var temperature by mutableStateOf("--°")
    var weatherSummary by mutableStateOf("날씨 불러오는 중")
    var hours by mutableStateOf<List<HourForecast>>(emptyList())
    var days by mutableStateOf<List<DayForecast>>(emptyList())
    var weatherDetails by mutableStateOf<List<WeatherDetail>>(emptyList())

    var events by mutableStateOf(listOf(EventRow("Google 계정 연결", "", Palette.Event)))
    var eventDots by mutableStateOf<Map<Int, List<Color>>>(emptyMap())
    var tasks by mutableStateOf<List<TaskRow>>(emptyList())
    var taskCounts by mutableStateOf<List<TaskCount>>(emptyList())
    var taskLists by mutableStateOf<List<TaskCount>>(emptyList())
    var mails by mutableStateOf(listOf(MailRow("Google", "", "계정 연결", "", false)))
    var dms by mutableStateOf<List<DmRow>>(emptyList())
    var news by mutableStateOf<List<NewsRow>>(emptyList())
    var newsFeature by mutableStateOf(NewsRow("뉴스 불러오는 중", ""))
    val newsChips = listOf("IT", "AI", "경제", "반도체", "교육")

    var parcels by mutableStateOf(listOf(ParcelRow("운송장 등록", "대기", "", 0, false)))
    var trackSteps by mutableStateOf<List<TrackStep>>(emptyList())

    var statusLine by mutableStateOf("경기 정보 조회 중")
    var scoreLine by mutableStateOf("오늘 경기")
    var countLine by mutableStateOf("")
    var innings by mutableStateOf((1..9).map { it.toString() })
    var inningsAway by mutableStateOf(List(9) { "" })
    var inningsHome by mutableStateOf(List(9) { "" })
    var PITCHER by mutableStateOf("-")
    var BATTER by mutableStateOf("-")
    var games by mutableStateOf<List<GameRow>>(emptyList())
    var standings by mutableStateOf<List<StandingRow>>(emptyList())
    val geminiPrompts = listOf("오늘 일정 요약해줘", "논문 핵심 3줄 정리", "저녁 메뉴 추천")
}
