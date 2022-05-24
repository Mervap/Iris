## Contribution

To run it, you'll have to:
1. Get a [Maps API key][api-key]
2. Add an entry in `local.properties` that looks like `MAPS_API_KEY=YOUR_KEY`
3. Build and run

Project structure is kotlin-multiplatform app and looks like:

Project
```
Project
├─ androidApp  
├─ iosApp
├─ shared/src
   ├─ commonMain
   ├─ androidMain
   ├─ iosMain
   ├─ commonTest
```

Where
* `androidApp` - module for Android UI. Written using [Jetbrains Compose][jb-compose] library
* `iosApp` - module for IOS Ui. Written using Swift UI
* `commonMain` - base app logic
* `androidMain` - android-specific logic
* `iosMain` - ios-specific logic
* `commonTest` - basic tests

[api-key]: https://developers.google.com/maps/documentation/android-sdk/get-api-key
[jb-compose]: https://www.jetbrains.com/lp/compose-mpp/