package dev.clanov.frontview.data

import android.util.Xml
import dev.clanov.frontview.ui.sample.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PublicSources(private val client: OkHttpClient = OkHttpClient()) {
    private fun get(url: String): String {
        val request = Request.Builder().url(url).header("User-Agent", "FrontView/0.1").build()
        return client.newCall(request).execute().use { res ->
            check(res.isSuccessful) { "HTTP " + res.code }
            checkNotNull(res.body).string()
        }
    }

    suspend fun weather(lat: Double = 37.5665, lon: Double = 126.9780) = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme("https").host("api.open-meteo.com")
            .addPathSegments("v1/forecast")
            .addQueryParameter("latitude", lat.toString())
            .addQueryParameter("longitude", lon.toString())
            .addQueryParameter("current", "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,is_day,wind_speed_10m")
            .addQueryParameter("hourly", "temperature_2m,weather_code,is_day")
            .addQueryParameter("daily", "temperature_2m_max,temperature_2m_min,weather_code")
            .addQueryParameter("forecast_days", "7")
            .addQueryParameter("timezone", "Asia/Seoul").build()
        val root = JSONObject(get(url.toString()))
        val now = root.getJSONObject("current")
        val temp = now.getDouble("temperature_2m")
        SampleData.temperature = temp.toInt().toString() + "°"
        val desc = skyName(now.getInt("weather_code"))
        val days = root.getJSONObject("daily")
        val high = days.getJSONArray("temperature_2m_max").getDouble(0).toInt()
        val low = days.getJSONArray("temperature_2m_min").getDouble(0).toInt()
        SampleData.weatherSummary = desc + " · 최고 " + high + "° 최저 " + low + "°"
        SampleData.weatherDetails = listOf(
            WeatherDetail("체감 온도", now.getDouble("apparent_temperature").toInt().toString() + "°"),
            WeatherDetail("습도", now.getInt("relative_humidity_2m").toString() + "%"),
            WeatherDetail("바람", now.getDouble("wind_speed_10m").toInt().toString() + " km/h")
        )
        val hourly = root.getJSONObject("hourly")
        val times = hourly.getJSONArray("time")
        val hourlyTemps = hourly.getJSONArray("temperature_2m")
        val codes = hourly.getJSONArray("weather_code")
        val daylight = hourly.getJSONArray("is_day")
        val currentHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0)
        val first = (0 until times.length()).indexOfFirst {
            runCatching { LocalDateTime.parse(times.getString(it)) >= currentHour }.getOrDefault(false)
        }.coerceAtLeast(0)
        SampleData.hours = (first until minOf(first + 6, times.length())).map {
            HourForecast(times.getString(it).takeLast(5).take(2) + "시", hourlyTemps.getDouble(it).toInt(),
                toSky(codes.getInt(it), daylight.getInt(it) == 1))
        }
        val labels = days.getJSONArray("time")
        val max = days.getJSONArray("temperature_2m_max")
        val min = days.getJSONArray("temperature_2m_min")
        val dayCodes = days.getJSONArray("weather_code")
        SampleData.days = (0 until labels.length()).map {
            val date = LocalDate.parse(labels.getString(it))
            DayForecast(if (it == 0) "오늘" else date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.KOREAN),
                toSky(dayCodes.getInt(it), true), min.getDouble(it).toInt(), max.getDouble(it).toInt())
        }
    }

    private fun skyName(code: Int) = when (code) {
        0, 1 -> "맑음"
        2 -> "구름 조금"
        3 -> "흐림"
        in 51..67, in 80..82 -> "비"
        in 71..77, in 85..86 -> "눈"
        in 95..99 -> "뇌우"
        else -> "흐림"
    }

    private fun toSky(code: Int, isDay: Boolean) =
        if (code < 2) { if (isDay) Sky.Sun else Sky.Moon } else Sky.Cloud

    suspend fun news(query: String = "IT AI 경제") = withContext(Dispatchers.IO) {
        val url = HttpUrl.Builder().scheme("https").host("news.google.com")
            .addPathSegments("rss/search").addQueryParameter("q", query)
            .addQueryParameter("hl", "ko").addQueryParameter("gl", "KR")
            .addQueryParameter("ceid", "KR:ko").build()
        val parser = Xml.newPullParser()
        parser.setInput(StringReader(get(url.toString())))
        val rows = mutableListOf<NewsRow>()
        var inItem = false
        var title = ""
        var source = ""
        var link = ""
        while (parser.eventType != XmlPullParser.END_DOCUMENT && rows.size < 20) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "item" -> { inItem = true; title = ""; source = ""; link = "" }
                    "title" -> if (inItem) title = parser.nextText()
                    "source" -> if (inItem) source = parser.nextText()
                    "link" -> if (inItem) link = parser.nextText()
                }
            } else if (parser.eventType == XmlPullParser.END_TAG && parser.name == "item") {
                if (title.isNotBlank()) rows.add(NewsRow(title, source, link))
                inItem = false
            }
            parser.next()
        }
        if (rows.isNotEmpty()) {
            SampleData.newsFeature = rows.first()
            SampleData.news = rows.drop(1)
        }
    }

    suspend fun track(key: String, carrier: String, invoice: String) = withContext(Dispatchers.IO) {
        require(key.isNotBlank() && carrier.isNotBlank() && invoice.isNotBlank())
        val url = HttpUrl.Builder().scheme("https").host("info.sweettracker.co.kr")
            .addPathSegments("api/v1/trackingInfo")
            .addQueryParameter("t_key", key)
            .addQueryParameter("t_code", carrier)
            .addQueryParameter("t_invoice", invoice)
            .build()
        val response = JSONObject(get(url.toString()))
        check(!response.has("status") || response.optBoolean("status")) {
            response.optString("msg", "배송조회 오류")
        }
        val code = response.optString("code")
        check(code == "0" || code == "200" || code == "") {
            "Parcel API code " + code
        }
        val done = response.optString("completeYN").equals("Y", true) || response.optBoolean("complete", false)
        val details = response.optJSONArray("trackingDetails")
        val steps = mutableListOf<TrackStep>()
        if (details != null) {
            for (i in 0 until details.length()) {
                val s = details.getJSONObject(i)
                steps.add(TrackStep(
                    label = s.optString("kind", "배송 정보"),
                    place = s.optString("where"),
                    time = s.optString("timeString"),
                    reached = true,
                    current = i == details.length() - 1
                ))
            }
        }
        val status = steps.lastOrNull()?.label
            ?: if (done) "배송 완료" else "조회됨"
        val progress = when {
            done -> 5
            status.contains("배송 출발") -> 4
            status.contains("터미널") || status.contains("이동") -> 3
            steps.isNotEmpty() -> (details?.optJSONObject(details.length() - 1)?.optInt("level") ?: 1).coerceIn(1, 5)
            else -> 0
        }
        SampleData.parcels = listOf(
            ParcelRow(response.optString("itemName").ifBlank { "운송장 " + invoice },
                status, carrier + " · " + invoice, progress, done)
        )
        SampleData.trackSteps = steps
    }

    suspend fun baseball(date: LocalDate = LocalDate.now()) = withContext(Dispatchers.IO) {
        val day = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val url = HttpUrl.Builder().scheme("https").host("api-gw.sports.naver.com")
            .addPathSegments("schedule/games").addQueryParameter("upperCategoryId", "kbaseball")
            .addQueryParameter("fromDate", day).addQueryParameter("toDate", day).build()
        val envelope = JSONObject(get(url.toString()))
        check(envelope.optBoolean("success", true))
        val games = envelope.getJSONObject("result").getJSONArray("games")
        val rows = (0 until games.length()).map { games.getJSONObject(it) }
            .filter { it.optString("categoryId") == "kbo" }
        SampleData.games = rows.map {
            GameRow(
                it.optString("awayTeamName") + " vs " + it.optString("homeTeamName"),
                it.optString("statusInfo").ifBlank { it.optString("statusCode") } +
                    " " + it.optString("awayTeamScore", "-") + ":" + it.optString("homeTeamScore", "-"),
                it.optString("statusCode") == "STARTED"
            )
        }
        val active = rows.firstOrNull { it.optString("statusCode") == "STARTED" }
            ?: rows.firstOrNull()
        if (active == null) {
            SampleData.statusLine = "경기 없음"
            SampleData.scoreLine = "오늘 경기가 없습니다"
            SampleData.countLine = ""
            SampleData.PITCHER = "-"
            SampleData.BATTER = "-"
            SampleData.inningsAway = List(9) { "" }
            SampleData.inningsHome = List(9) { "" }
            return@withContext
        }
        val status = active.optString("statusInfo").ifBlank { active.optString("statusCode") }
        SampleData.statusLine = status
        SampleData.scoreLine = active.optString("awayTeamName") + "  " +
            active.optString("awayTeamScore") + " : " + active.optString("homeTeamScore") + "  " +
            active.optString("homeTeamName")
        SampleData.PITCHER = active.optString("homeCurrentPitcherName").ifBlank { "-" }
        SampleData.BATTER = "-"
        if (active.optString("statusCode") != "STARTED") {
            SampleData.countLine = ""
            return@withContext
        }
        val gameId = active.optString("gameId")
        if (gameId.isBlank()) return@withContext
        val relayUrl = "https://api-gw.sports.naver.com/schedule/games/" + gameId + "/relay"
        val relay = JSONObject(get(relayUrl)).optJSONObject("result")?.optJSONObject("textRelayData")
            ?: return@withContext
        val state = relay.optJSONObject("currentGameState")
        if (state != null) {
            SampleData.PITCHER = state.optString("pitcher").ifBlank { "-" }
            SampleData.BATTER = state.optString("batter").ifBlank { "-" }
            val base = (1..3).filter { state.optString("base" + it).isNotBlank() }
            SampleData.countLine = "B" + state.optString("ball", "0") +
                " S" + state.optString("strike", "0") +
                " O" + state.optString("out", "0") +
                " · " + if (base.isEmpty()) "주자 없음" else base.joinToString(", ") + "루"
        }
        val innings = relay.optJSONObject("inningScore")
        fun scores(side: String): List<String> {
            val values = innings?.optJSONObject(side)
            return (1..9).map { values?.optString(it.toString()) ?: "" }
        }
        SampleData.inningsAway = scores("away")
        SampleData.inningsHome = scores("home")
    }
}
