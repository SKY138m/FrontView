# FrontView

Android home launcher for Toss Front 2 (LineageOS).

## Stack
Kotlin · Jetpack Compose · Android API 35 · Apache-2.0

## Home
- 7 slots on an 800 x 1280 design grid; any widget can go in any slot, or leave a slot empty
- Widgets: clock, weather, calendar, tasks, Apple Music, Gemini, Gmail, Instagram DM, Google News, delivery tracking, KBO monitor
- Tap a widget for its fullscreen view; Gemini opens its app
- Swap button (bottom right) turns on edit mode; tap a slot to pick a widget
- Only the clock is live. Other widgets show sample data from `ui/sample/SampleData.kt`

## Build
JDK 17, Android SDK 35, Gradle 8.11.1

    gradle :app:assembleDebug

## Integrations
See [docs/integrations.md](docs/integrations.md).

## License
Apache-2.0
