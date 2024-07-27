package com.example.threadapp.Navigation

sealed class Routes(val routes:String) {
    data object Home : Routes("home")
    data object Notification : Routes("notification")
    data object Profile: Routes("profile")
    data object Search : Routes("search")
    data object Splash : Routes("splash")
    data object AddThread: Routes("addThread")
    data object BottomNav : Routes("bottomNav")
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object OtherUsers : Routes("other_users/{data}")
}