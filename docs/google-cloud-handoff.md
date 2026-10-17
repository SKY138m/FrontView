# Google Cloud handoff

Repository: `SKY138m/FrontView`

- Android package: `dev.clanov.frontview`
- API: Google Calendar API, Google Tasks API, Gmail API
- OAuth: Android client ID, package + SHA-1 signing certificate
- Scopes: `calendar.events.readonly`, `tasks`, `gmail.readonly`
- OAuth callback: `com.googleusercontent.apps.<client-id-prefix>:/oauth2redirect`

Set `GOOGLE_OAUTH_CLIENT_ID=<client-id>.apps.googleusercontent.com` in local Gradle properties (`~/.gradle/gradle.properties`) or provide `-PGOOGLE_OAUTH_CLIENT_ID=...`.

Next: create the Google Cloud project, enable the APIs, configure the OAuth consent screen and Android client, and register each signing certificate SHA-1 used for device builds. Test sign-in and the Calendar/Tasks/Gmail APIs.

Gmail uses a restricted OAuth scope. Review Google's verification requirements before public distribution.
