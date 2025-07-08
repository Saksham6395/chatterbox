🧵 Thread App Clone
A full-stack Threads-like social media app built with Jetpack Compose and Firebase. Users can register, login, post threads with images, follow/unfollow others, and receive real-time notifications. The app also includes a filtered search system and personalized profiles.

📁 Table of Contents
About the Project

Features

Tech Stack

Setup Guide

App Structure

Deployment

License

Author

🧠 About the Project
This app replicates key functionality from Instagram Threads, with full Firebase integration:

Register & login users using Firebase Auth

Add threads with text and image

Follow/unfollow users and view live follower counts

Real-time notifications for follow/unfollow events

Search users by name or username

View personal and other users' profiles with their threads

🎯 Features
Feature	Description
🔐 Authentication	Register and login with Firebase Email/Password
🔃 Session Caching	Persistent login using ViewModel + SharedPreferences
🧵 Post Threads	Users can post text-based threads with optional images
🖼️ Image Picker	Add thread images using Android media picker
🔎 Search Users	Real-time filtered search by name or username
👥 Follow System	Follow/unfollow users; live follower/following count
🔔 Notifications	Real-time notification for follow/unfollow events
🙍‍♂️ Profile View	View your own or others' profiles with all their threads
🧭 Navigation	Bottom Navigation bar powered by Jetpack Compose Navigation
📦 Firebase Integration	Uses Firebase Auth, Firestore, Realtime Database
🪄 Modern UI	Built using Jetpack Compose with Material3

🛠️ Tech Stack
Layer	Tool
🧱 UI	Jetpack Compose, Material 3
🔥 Backend	Firebase Auth, Firestore, Realtime Database
🧠 Logic	ViewModel, LiveData, Kotlin
📦 Caching	SharedPreferences or DataStore
📷 Image Picker	Android MediaStore APIs
🧪 Testing	(Optional) JUnit, Espresso

🚀 Setup Guide
Clone the Repo

bash
Copy
Edit
git clone https://github.com/yourusername/thread-app-clone.git
cd thread-app-clone
Open in Android Studio

Configure Firebase

Add google-services.json to the app/ directory

Enable Email/Password authentication in Firebase Auth

Create Firestore & Realtime DB instances

Run the App 🎉

📂 App Structure
bash
Copy
Edit
com.example.threadapp/
├── item_view/                
│   ├── ThreadItem.kt
│   └── UserItem.kt
├── model/                    # Data models
│   ├── BottomNavitem.kt
│   ├── NotificationModel.kt
│   ├── ThreadModel.kt
│   └── UserModel.kt
├── Navigation/               
│   ├── Navgraph.kt
│   └── Routes.kt
├── Screen/                   # UI screens
│   ├── Addthreads.kt
│   ├── BottomNav.kt
│   ├── HomeScreen.kt
│   ├── login.kt / register.kt
│   ├── Profile.kt / otherUser.kt
│   ├── Notification.kt
│   ├── Search.kt
│   └── Splash.kt
├── ui.theme/                 # App theme styling
├── utils/
│   └── sharedPref.kt         # Session storage
├── viewmodel/                # All ViewModels
│   ├── AddThreadViewModel.kt
│   ├── AuthViewModel.kt
│   ├── HomeViewModel.kt
│   ├── NotificationViewModel.kt
│   ├── ProfileViewModel.kt
│   └── UserViewModel.kt
└── MainActivity.kt           # App entry point
🌐 Deployment
You can:

Build the APK from Android Studio

Upload to Google Drive or Firebase App Distribution

Share the download link (via WhatsApp, Email, etc.)

(Optional) Record a demo video and upload to YouTube or Hugging Face Spaces

📝 License
MIT License — free to use, modify, and distribute. Star it, fork it, build on top of it 🚀

🙌 Author
Saksham
📍 ECE B.Tech @ SVNIT
⚔️ Competitive Programmer | 📱 Android Developer | 🤖 ML Enthusiast

GitHub | LinkedIn | Twitter

