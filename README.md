
# 🧵 Thread App Clone

A full-stack Threads-like **social media app** built with **Jetpack Compose** and **Firebase**. Users can register, login, post threads with images, follow/unfollow others, and receive real-time notifications. The app also includes a filtered search system and personalized profiles.

---

## 📁 Table of Contents

- [About the Project](#🧠-about-the-project)  
- [Features](#🎯-features)  
- [Tech Stack](#🛠️-tech-stack)  
- [Setup Guide](#🚀-setup-guide)  
- [App Structure](#📂-app-structure)  
- [Author](#🙌-author)

---

## 🧠 About the Project

This app replicates key functionality from Instagram Threads, with full Firebase integration:

- Register & login users using Firebase Auth  
- Add threads with text and image  
- Follow/unfollow users and view live follower counts  
- Real-time notifications for follow/unfollow events  
- Search users by name or username  
- View personal and other users' profiles with their threads  

---

## 🎯 Features

| Feature               | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| 🔐 Authentication     | Register and login with Firebase Email/Password                             |
| 🔃 Session Caching    | Persistent login using ViewModel + SharedPreferences                        |
| 🧵 Post Threads       | Users can post text-based threads with optional images                      |
| 🖼️ Image Picker       | Add thread images using Android media picker                                |
| 🔎 Search Users       | Real-time filtered search by name or username                               |
| 👥 Follow System      | Follow/unfollow users; live follower/following count                        |
| 🔔 Notifications      | Real-time notification for follow/unfollow events                           |
| 🙍‍♂️ Profile View      | View your own or others' profiles with all their threads                    |
| 🧭 Navigation         | Bottom Navigation bar powered by Jetpack Compose Navigation                 |
| 📦 Firebase Integration | Uses Firebase Auth, Firestore, Realtime Database                         |
| 🪄 Modern UI          | Built using Jetpack Compose with Material3                                  |

---

## 🛠️ Tech Stack

| Layer         | Tool                                                                 |
|---------------|----------------------------------------------------------------------|
| 🧱 UI         | Jetpack Compose, Material 3                                          |
| 🔥 Backend    | Firebase Auth, Firestore, Realtime Database                          |
| 🧠 Logic      | ViewModel, LiveData, Kotlin                                           |
| 📦 Caching    | SharedPreferences or DataStore                                       |
| 📷 Image Picker | Android MediaStore APIs                                            |
| 🧪 Testing    | (Optional) JUnit, Espresso                                           |

---

## 🚀 Setup Guide

1. **Clone the Repo**  
```bash
git clone https://github.com/yourusername/thread-app-clone.git
cd thread-app-clone
```

2. **Open in Android Studio**

3. **Configure Firebase**
   - Add `google-services.json` to the `app/` directory  
   - Enable Email/Password authentication in Firebase Auth  
   - Create Firestore & Realtime DB instances

4. **Run the App** 🎉

---

## 📂 App Structure

```
com.example.threadapp/
├── item_view/                
│   ├── ThreadItem.kt
│   └── UserItem.kt
├── model/                    
│   ├── BottomNavitem.kt
│   ├── NotificationModel.kt
│   ├── ThreadModel.kt
│   └── UserModel.kt
├── Navigation/               
│   ├── Navgraph.kt
│   └── Routes.kt
├── Screen/                   
│   ├── Addthreads.kt
│   ├── BottomNav.kt
│   ├── HomeScreen.kt
│   ├── login.kt
│   ├── register.kt
│   ├── otherUser.kt
│   ├── Profile.kt
│   ├── Notification.kt
│   ├── Search.kt
│   └── Splash.kt
├── ui.theme/                 
├── utils/
│   └── sharedPref.kt         
├── viewmodel/                
│   ├── AddThreadViewModel.kt
│   ├── AuthViewModel.kt
│   ├── HomeViewModel.kt
│   ├── NotificationViewModel.kt
│   ├── ProfileViewModel.kt
│   └── UserViewModel.kt
└── MainActivity.kt           
```

---


## 🙌 Author

**Saksham**  
📍 ECE B.Tech @ SVNIT  
⚔️ Competitive Programmer | 📱 Android Developer | 🤖 ML Enthusiast  

[GitHub](https://github.com/Saksham6395) | [LinkedIn](www.linkedin.com/in/saksham-samarth)
