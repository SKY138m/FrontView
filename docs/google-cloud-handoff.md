# Google Cloud

Project: `pront-homeview` (392826191328)

- Android package: `dev.clanov.frontview`
- APIs enabled: Google Calendar API, Google Tasks API, Gmail API
- Consent screen: FrontView, external testing
- Test user: project owner's Google account
- Android client ID: `392826191328-uevhhvs5lm0tg4co1jocsn47gtcrcf82.apps.googleusercontent.com`
- Cloud Shell debug signing SHA-1: `A8:35:EF:7F:64:58:8D:AB:12:F6:67:56:60:B6:10:D6:A1:74:31:18`
- Scopes: `calendar.events.readonly`, `tasks`, `gmail.readonly`
- Callback: `com.googleusercontent.apps.392826191328-uevhhvs5lm0tg4co1jocsn47gtcrcf82:/oauth2redirect`

Set this in local `~/.gradle/gradle.properties`:

    GOOGLE_OAUTH_CLIENT_ID=392826191328-uevhhvs5lm0tg4co1jocsn47gtcrcf82.apps.googleusercontent.com

The registered debug keystore is private in Cloud Shell at `~/.android/debug.keystore`. Reuse it for device builds. Other signing certificates need separate Android OAuth clients with their own SHA-1 and matching client ID. GitHub Actions' default debug certificate is different.

The AppAuth callback requires enabling Custom URI Scheme in the Android client's advanced settings. Google disables it by default; enable it only for this flow. Offline access and consent are requested so Google can issue a refresh token.

Gmail uses a restricted scope. Review Google's verification requirements before public distribution. Device sign-in and the three API calls require end-to-end testing.
