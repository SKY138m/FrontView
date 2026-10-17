package dev.clanov.frontview.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class Weather(
    val temperatureCelsius: Double,
    val weatherCode: Int,
    val isDay: Boolean
)

class OpenMeteoClient(private val client: OkHttpClient = OkHttpClient()) {
    suspend fun current(latitude: Double, longitude: Double): Result<Weather> =
        withContext(Dispatchers.IO) {
            runCatching {
                require(latitude in -90.0..90.0 && longitude in -180.0..180.0)
                val url = HttpUrl.Builder()
                    .scheme("https")
                    .host("api.open-meteo.com")
                    .addPathSegments("v1/forecast")
                    .addQueryParameter("latitude", latitude.toString())
                    .addQueryParameter("longitude", longitude.toString())
                    .addQueryParameter("current", "temperature_2m,weather_code,is_day")
                    .addQueryParameter("timezone", "Asia/Seoul")
                    .build()

                client.newCall(Request.Builder().url(url).build()).execute().use { response ->
                    check(response.isSuccessful) { "Weather HTTP " + response.code }
                    val current = JSONObject(checkNotNull(response.body).string())
                        .getJSONObject("current")
                    Weather(
                        temperatureCelsius = current.getDouble("temperature_2m"),
                        weatherCode = current.getInt("weather_code"),
                        isDay = current.getInt("is_day") == 1
                    )
                }
            }
        }
}
