# Realtime Location Tracker Module

**Realtime Location Tracker** is a Kotlin Android module that provides **offline-first location tracking and reliable remote sync**. The module is designed to be **reusable in any app**, handling all background logic, database storage, network-aware syncing, and cleanup automatically. The UI layer is optional and can be customized to show status or control tracking.

---

## Features

### Core Module
- **Background Location Collection**
  - Collects device location every 5 seconds using a **foreground service**.
  - Handles permissions (`ACCESS_FINE_LOCATION`, `ACCESS_BACKGROUND_LOCATION`).

- **Offline-first Storage**
  - Stores location entries in a **Room database**.
  - Each entry marked as **pending sync** until successfully uploaded.

- **Reliable Remote Sync**
  - Syncs unsynced data automatically when the network is available.
  - Marks entries as synced after successful POST to remote server.

- **Periodic Cleanup**
  - WorkManager deletes already synced data every hour to save storage.

- **Network Awareness**
  - Pauses syncing when offline and resumes automatically when online.

- **Cancellation-safe**
  - Uses **coroutines** to ensure services stop cleanly and no memory leaks occur.

---

### UI Layer (Optional)
- **Start/Stop Tracking Buttons**
  - Control location tracking from the UI.
- **Unsynced Data Count**
  - Shows the number of pending location entries in real-time.
- **Offline/Online Indicator**
  - Visual indicator of network connectivity.

> The UI is **optional**; the module works independently in the background.

---

## Architecture Overview
```
UI Layer
|
v
+-------------------+
| Location Module |
| (LocationTrackingManager) |
+-------------------+
|
v
+-------------------+
| Service + Worker |
+-------------------+
|
v
+-------------------+
| LocationRepository|
+-------------------+
|
v
+-------------------+
| Room Database |
+-------------------+
|
v
+-------------------+
| LocationSyncService|
+-------------------+
|
v
+---------------------+
| Remote Server API |
+---------------------+
```

## Installation

1. **Add Module**
   - Copy the module code (`location-tracker-core`) into your project or add as a Gradle dependency.

2. **Methods in TrackingMnager**
   - startTracking() for starting the location tracking
   - stopTracking() for stopping the location tracking
   - startSync() for start the syncing of data to remote
   - startCleanUpWorker() for cleaning the synced data after 1 hour

   these methods requires these permission so please make sure to show permission dialog for them
   - ACCESS_BACKGROUND_LOCATION
   - ACCESS_FINE_LOCATION
   - ACCESS_COARSE_LOCATION
   - POST_NOTIFICATIONS
