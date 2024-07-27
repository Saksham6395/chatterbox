package com.example.threadapp.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadapp.Screen.AddThread
import com.example.threadapp.Screen.BottomNav
import com.example.threadapp.Screen.Home
import com.example.threadapp.Screen.Notification
import com.example.threadapp.Screen.OtherUserProfile
import com.example.threadapp.Screen.Profile
import com.example.threadapp.Screen.Search
import com.example.threadapp.Screen.Splash
import com.example.threadapp.Screen.login
import com.example.threadapp.Screen.register
import com.example.threadapp.viewmodel.AuthViewModel

@Composable
fun NavGraph( navController: NavHostController, authViewModel: AuthViewModel){
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes){
            Splash(navController,authViewModel)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.AddThread.routes){
            AddThread(navController)
        }
        composable(Routes.Profile.routes){
            Profile(navController,authViewModel)
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController,authViewModel)
        }
        composable(Routes.Login.routes){
            login(navController,authViewModel)
        }
        composable(Routes.Register.routes){
            register(navController,authViewModel)
        }
        composable(Routes.OtherUsers.routes){
            val data=it.arguments!!.getString("data")
            OtherUserProfile(navController, authViewModel, data!!)
        }
    }
}
