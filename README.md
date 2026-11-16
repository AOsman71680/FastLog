# FastLog

Intermittent fasting tracker. Built as a web app (`www/`) wrapped into an Android app with Capacitor.

## Get the APK (no Android Studio needed)
1. Create a free account at github.com and make a new **private** repository.
2. Upload this whole folder to it (or `git push`).
3. Open the **Actions** tab, choose **Build APK**, press **Run workflow**. (It also runs on every push.)
4. After about 5-8 minutes open the finished run and download the **FastLog-APK** artifact (a zip).
5. Unzip it, copy `app-debug.apk` to your phone and open it. Allow "install unknown apps" when asked.
6. Long-press the home screen -> Widgets -> **FastLog** to add the fasting widget.

## Build locally instead
Needs JDK 21 and the Android SDK (Android Studio installs both).

    npm install
    npx cap sync android
    cd android && ./gradlew assembleDebug

APK: `android/app/build/outputs/apk/debug/app-debug.apk`

## Layout
- `www/` the app (HTML/JS/CSS). Edit here, then run `npx cap sync android`.
- `android/app/src/main/java/com/fastlog/app/` native widget (`FastWidgetProvider`) and the bridge that feeds it (`WidgetBridgePlugin`).
