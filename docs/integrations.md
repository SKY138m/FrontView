# Integrations

| Module | Source | Method |
| --- | --- | --- |
| Weather | Open-Meteo | HTTPS current forecast |
| Calendar | Google Calendar API | OAuth 2.0 |
| Tasks | Google Tasks API | OAuth 2.0 |
| Gmail | Gmail API | OAuth 2.0 |
| Apple Music | Android MediaSession | Notification access |
| Instagram | Android notifications | Notification access |
| News | Google News RSS | HTTPS RSS |
| Delivery | SweetTracker | API key |
| Baseball | KBO + Naver Sports | Website / undocumented API |
| Muse | Muse app | Android app launch |

## Google
- Calendar: `GET https://www.googleapis.com/calendar/v3/calendars/primary/events`
- Tasks lists: `GET https://tasks.googleapis.com/tasks/v1/users/@me/lists`
- Tasks: `GET https://tasks.googleapis.com/tasks/v1/lists/{tasklist}/tasks`
- Gmail: `GET https://gmail.googleapis.com/gmail/v1/users/me/messages`
- OAuth uses an external browser and PKCE. Store tokens securely.
- Configure Android package + signing SHA-1 in Google Cloud.

## Local Android
- Apple Music package: `com.apple.android.music`
- Instagram package: `com.instagram.android`
- Notification listener requires user-granted access.
- MediaSession requires an accessible active session.

## Public data
- Weather: `https://api.open-meteo.com/v1/forecast`
- News: `https://news.google.com/rss/search?q=AI&hl=ko&gl=KR&ceid=KR:ko`
- SweetTracker: company code + tracking number + API key.
- KBO scoreboard: `https://www.koreabaseball.com/schedule/scoreboard.aspx`
- Naver Sports undocumented gateway: `https://api-gw.sports.naver.com/schedule/games?upperCategoryId=kbaseball&fromDate=...&toDate=...`
- Naver relay: `/schedule/games/{gameId}/relay`

## References
- [Calendar](https://developers.google.com/workspace/calendar/api/v3/reference/events/list)
- [Tasks](https://developers.google.com/workspace/tasks/reference/rest/v1/tasks/list)
- [Gmail](https://developers.google.com/workspace/gmail/api/reference/rest/v1/users.messages/list)
- [Android MediaSession](https://developer.android.com/reference/android/media/session/MediaSessionManager)
- [Open-Meteo](https://open-meteo.com/en/docs)
- [SweetTracker](https://tracking.sweettracker.co.kr/)
- [KBO gateway example](https://github.com/jeonbyeongmin/kbo-cli)
