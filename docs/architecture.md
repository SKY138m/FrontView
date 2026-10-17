# Architecture

`home/`: launcher activity state, slot selection, settings

`data/`: normalized models and external data contracts

`integrations/`: Android notification and media sessions

User settings: DataStore. API caches: Room when persistence is required.

Public APIs: HTTPS with explicit refresh intervals and offline fallback.

Credentials: Android Keystore-backed storage. Never commit tokens, OAuth refresh tokens, Apple keys, or tracking API keys.

UI ownership: Claude design; replace HomeScreen while preserving data contracts.
