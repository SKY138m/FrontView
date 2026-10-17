package dev.clanov.frontview.ui.sample

import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.Palette

enum class Sky { Sun, Cloud, Moon }

data class HourForecast(val label: String, val temp: Int, val sky: Sky)
data class DayForecast(val name: String, val sky: Sky, val low: Int, val high: Int)
data class EventRow(val title: String, val time: String, val color: Color)
data class TaskRow(val title: String, val done: Boolean)
data class TaskCount(val name: String, val count: Int, val color: Color)
data class MailRow(val from: String, val time: String, val subject: String, val preview: String, val unread: Boolean)
data class DmRow(val initial: String, val name: String, val time: String, val message: String, val unread: Boolean)
data class NewsRow(val title: String, val source: String)
data class ParcelRow(val name: String, val status: String, val sub: String, val progress: Int, val delivered: Boolean)
data class TrackStep(val label: String, val place: String, val time: String, val reached: Boolean, val current: Boolean)
data class GameRow(val match: String, val state: String, val live: Boolean)
data class StandingRow(val rank: String, val team: String, val record: String, val pct: String)
data class WeatherDetail(val label: String, val value: String, val unit: String = "")

/** Placeholder content that matches the design mock-ups. Replace with the sources in `data/`. */
object SampleData {
    val hours = listOf(
        HourForecast("15시", 20, Sky.Sun),
        HourForecast("16시", 20, Sky.Sun),
        HourForecast("17시", 18, Sky.Cloud),
        HourForecast("18시", 16, Sky.Cloud),
        HourForecast("19시", 14, Sky.Moon),
        HourForecast("20시", 13, Sky.Moon)
    )

    val days = listOf(
        DayForecast("오늘", Sky.Sun, 11, 22),
        DayForecast("일", Sky.Cloud, 12, 19),
        DayForecast("월", Sky.Sun, 9, 21),
        DayForecast("화", Sky.Sun, 8, 20),
        DayForecast("수", Sky.Cloud, 10, 17),
        DayForecast("목", Sky.Sun, 7, 18),
        DayForecast("금", Sky.Sun, 6, 19)
    )

    val weatherDetails = listOf(
        WeatherDetail("체감 온도", "18°"),
        WeatherDetail("습도", "52%"),
        WeatherDetail("바람", "3 m/s"),
        WeatherDetail("자외선", "4", "보통")
    )

    val events = listOf(
        EventRow("스터디", "15:30 – 17:00", Palette.Event),
        EventRow("저녁 식사", "19:00 – 20:00", Palette.Green),
        EventRow("운동", "21:00 – 22:00", Palette.Orange)
    )

    /** Day of month -> event dot colours shown in the month view. */
    val eventDots: Map<Int, List<Color>> = mapOf(
        3 to listOf(Palette.Event),
        8 to listOf(Palette.Green),
        12 to listOf(Palette.Orange),
        15 to listOf(Palette.Event),
        17 to listOf(Palette.Event, Palette.Green, Palette.Orange),
        20 to listOf(Palette.Green),
        24 to listOf(Palette.Event),
        28 to listOf(Palette.Orange, Palette.Event)
    )

    val tasks = listOf(
        TaskRow("과제 제출", false),
        TaskRow("논문 읽기", false),
        TaskRow("운동 가기", false),
        TaskRow("방 청소", true)
    )

    val taskCounts = listOf(
        TaskCount("예정", 8, Palette.Red),
        TaskCount("장보기", 1, Palette.GreenDark),
        TaskCount("심부름", 2, Palette.Purple),
        TaskCount("내 할 일", 7, Palette.Blue)
    )

    val taskLists = listOf(
        TaskCount("운동", 3, Palette.Purple),
        TaskCount("쇼핑", 0, Palette.Blue)
    )

    val mails = listOf(
        MailRow("학과 사무실", "14:20", "2학기 수강 정정 안내", "정정 기간은 다음 주 금요일까지입니다.", true),
        MailRow("팀 프로젝트", "12:05", "중간 발표 자료 공유", "슬라이드 초안 올렸어요. 확인 부탁드립니다.", true),
        MailRow("서비스 알림", "09:41", "새 기기에서 로그인했습니다", "본인이 아니라면 비밀번호를 변경하세요.", true),
        MailRow("뉴스레터", "어제", "이번 주 읽을거리", "개발·디자인 소식을 모았습니다.", true),
        MailRow("도서관", "어제", "대출 도서 반납 안내", "반납 기한이 3일 남았습니다.", false),
        MailRow("동아리", "10/15", "이번 주 모임 장소 변경", "장소가 학생회관 2층으로 바뀌었습니다.", false),
        MailRow("택배 알림", "10/14", "상품이 접수되었습니다", "배송 상태는 앱에서 확인하세요.", false)
    )

    val dms = listOf(
        DmRow("민", "민지", "14:48", "오늘 스터디 몇 시에 시작해?", true),
        DmRow("준", "준호", "13:30", "사진 보냈어, 확인해봐", true),
        DmRow("서", "서연", "11:12", "내일 점심 같이 먹을래?", true),
        DmRow("동", "동아리 단톡", "어제", "공지: 이번 주 모임 장소 변경", false),
        DmRow("지", "지훈", "어제", "ㅋㅋㅋ 그거 나도 봤어", false),
        DmRow("하", "하은", "10/15", "노트 공유해줘서 고마워!", false)
    )

    val news = listOf(
        NewsRow("반도체 수출 3개월 연속 증가, 올해 최대 규모", "[언론사] · 25분 전"),
        NewsRow("대학 연구팀, 저전력 AI 칩 설계 기법 발표", "[언론사] · 1시간 전"),
        NewsRow("환율 안정세 속 코스피 소폭 상승 마감", "[언론사] · 2시간 전"),
        NewsRow("주말 전국 대체로 맑음, 일교차 10도 이상", "[언론사] · 3시간 전")
    )
    val newsFeature = NewsRow("새 AI 모델 공개, 개발자 도구 대폭 개선", "[언론사] · 12분 전")
    val newsChips = listOf("IT", "AI", "경제", "반도체", "교육")

    val parcels = listOf(
        ParcelRow("무선 이어폰", "배송 출발", "택배사 A · 오늘 도착 예정", 4, false),
        ParcelRow("전공 서적", "이동 중", "택배사 B · 내일 도착 예정", 3, false),
        ParcelRow("모니터 받침대", "배송 완료", "택배사 A · 10/16 완료", 5, true)
    )

    val trackSteps = listOf(
        TrackStep("상품 접수", "발송지 물류센터", "10/15 18:20", reached = true, current = false),
        TrackStep("터미널 도착", "수도권 터미널", "10/16 03:10", reached = true, current = false),
        TrackStep("지역 터미널 도착", "서울 강서 지점", "10/17 06:30", reached = true, current = false),
        TrackStep("배송 출발", "배송 기사 배정", "10/17 08:45", reached = true, current = true),
        TrackStep("배송 완료", "오늘 도착 예정", "", reached = false, current = false)
    )

    val innings = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    val inningsAway = listOf("0", "1", "0", "0", "2", "0", "0", "", "")
    val inningsHome = listOf("1", "0", "0", "1", "0", "0", "", "", "")

    const val PITCHER = "원태인"
    const val BATTER = "구자욱"

    val games = listOf(
        GameRow("팀 A vs 팀 B", "7회 말 3:2", true),
        GameRow("팀 C vs 팀 D", "5회 초 1:1", true),
        GameRow("팀 E vs 팀 F", "종료 6:4", false),
        GameRow("팀 G vs 팀 H", "17:00 예정", false)
    )

    val standings = listOf(
        StandingRow("1", "팀 A", "82승 58패", ".586"),
        StandingRow("2", "팀 C", "79승 61패", ".564"),
        StandingRow("3", "팀 E", "75승 65패", ".536")
    )

    val geminiPrompts = listOf("오늘 일정 요약해줘", "논문 핵심 3줄 정리", "저녁 메뉴 추천")
}
