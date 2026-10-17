# Architecture

`home/`: widget list, 7-slot layout, settings, home screen

`ui/`: design system (800 x 1280 scale, palette, icons), `widgets/` (M, L, XL sizes), `screens/` (fullscreen views), `sample/` (placeholder data)

`data/`: normalized models and external data contracts

`integrations/`: Android notification and media sessions

User settings: DataStore. API caches: Room when persistence is required.

Public APIs: HTTPS with explicit refresh intervals and offline fallback.

Credentials: Android Keystore-backed storage. Never commit tokens, OAuth refresh tokens, Apple keys, or tracking API keys.

UI ownership: Claude design. To wire real data, replace the `SampleData` reads in `ui/widgets` and `ui/screens` with the sources in `data/`.
