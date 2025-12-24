# Realtime Location Tracker Module

**Realtime Location Tracker** is a Kotlin Android module that provides **offline-first location tracking and reliable remote sync**. The module is designed to be **reusable in any Android app**, handling all background logic, database storage, network-aware syncing, and cleanup automatically. The optional UI layer can be used to monitor status and control tracking.

---

## Features

### Core Module
- **Background Location Collection**
  - Collects device location every **5 seconds** using a **foreground service**.
  - Handles location and notification permissions (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_BACKGROUND_LOCATION`, `POST_NOTIFICATIONS`).

- **Offline-first Storage**
  - Locations are saved in a **Room database** as a proof-of-concept single source of truth.
  - Each entry is marked as **pending sync** until successfully uploaded to the remote server.

- **Reliable Remote Sync**
  - The **LocationSyncService** automatically syncs unsynced data when the network is available.
  - After a successful POST request, entries are **marked as synced** in the database.
  - Network awareness ensures that syncing is paused offline and resumes automatically online.

- **Periodic Cleanup**
  - A **WorkManager** class deletes already synced entries from the database every **hour**, ensuring storage stays optimized.

- **Cancellation-safe**
  - Uses **coroutines** to handle background tasks safely.
  - Properly handles cancellation to prevent memory leaks or duplicated work.

---

### Optional UI Layer
- **Start/Stop Tracking Buttons**
  - Provides user control to start or stop location tracking.
- **Unsynced Data Count**
  - Displays the number of location entries pending sync.
- **Offline/Online Indicator**
  - Shows current network connectivity status.

> The UI is **thin** and optional. The module works independently in the background.

---

## Architecture Overview

```text
UI Layer (Optional)
      |
      v
+---------------------------+
| Location Module           |
| (LocationTrackingManager) |
+---------------------------+
      |
      v
+-------------------+
| Service + Worker  |
| (LocationSyncService + Cleanup Worker) |
+-------------------+
      |
      v
+-------------------+
| LocationRepository|
+-------------------+
      |
      v
+-------------------+
| Room Database     |
+-------------------+
      |
      v
+---------------------+
| Remote Server API   |
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


- **Endpoint:** `POST https://free.mockerapi.com/post` (replace with your actual server URL)  
- **Headers:**  
  ```http
  Content-Type: application/json
  ```


  ## Assumptions and Limitations

- **Location Services:** Requires UI layer to show the permission Dialog currently.

- **Network Dependency:** Syncing requires network connectivity; offline entries remain in the local database until connectivity is restored.

- **Cleanup Interval:** Fixed at 1 hour for deleting synced entries (customizable via WorkManager).

- **Data Security:** Currently does not implement encryption; for production, use HTTPS + encrypted local storage.

- **Battery Usage:** Frequent updates (every 5 seconds) may impact battery life. Adjust interval if needed.

- **UI Layer:** Minimal and optional; the module is primarily backend-focused.

