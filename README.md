# RouteBoost Android — 0.2.0

A native Kotlin Android starter app for a Minecraft Bedrock network optimizer.

## Current build
- Native Android application with a custom RouteBoost launcher icon.
- Dark purple dashboard UI.
- Android VPN consent integration.
- Whole-device profile selected.

## Important limitation
This is **not yet an ExitLag replacement**. The VPN service is only a placeholder: there is no TUN packet forwarding, relay server, smart route selection, or live Minecraft server measurement. The dashboard does not invent ping or packet-loss values. Granting VPN permission does not mean the VPN is connected.

## Build an APK without a PC (GitHub Actions)
1. Sign in to GitHub in your phone browser and create a new repository.
2. Upload the contents of this project to the repository, keeping the `.github/workflows/android.yml` file in place. GitHub's website may require selecting the extracted project files rather than uploading this ZIP as one file.
3. Open the repository's **Actions** tab.
4. Select **Build RouteBoost APK**, then **Run workflow**.
5. When the run finishes, open it and download the **RouteBoost-debug-apk** artifact. Extract the artifact ZIP; inside is `app-debug.apk`.
6. Tap the APK to install. Android may ask you to allow installs from the browser or Files app you used.

The included GitHub Actions workflow builds a debug APK using Java 17, Gradle 8.9, and Android SDK 35.

## Local build
Open in Android Studio and run `gradle assembleDebug` (Gradle 8.9, JDK 17, Android SDK 35).
