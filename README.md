# 🎵 GaniMusicApp

**GaniMusicApp** is an Android music streaming application built using Kotlin. It fetches and plays songs from the [Jamendo Developer API](https://developer.jamendo.com/v3.0) and displays metadata like title, artist, and album art.

## 🔧 Tech Stack
- **Kotlin** – Modern Android app development  
- **Retrofit** – For API communication  
- **ExoPlayer** – For seamless audio streaming  
- **Glide** – To load song cover images  
- **MVVM (planned)** – Code architecture (optional enhancement)

## 🎤 Features
- Fetches free music from Jamendo API  
- Displays song title, artist name, and image  
- Streams music using ExoPlayer  
- RecyclerView to browse available songs  

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/SABAVATH-GANESH/GaniMusicApp.git
   ```

2. Open in **Android Studio**

3. Add your Jamendo API Client ID in your API file:
   ```kotlin
   const val JAMENDO_CLIENT_ID = "your_client_id"
   ```

4. Run the app on an emulator or real device.

## 📸 Screenshots
_(Coming soon after UI setup)_

## 📚 API Reference

- **Jamendo API Docs**: [https://developer.jamendo.com/v3.0](https://developer.jamendo.com/v3.0)
