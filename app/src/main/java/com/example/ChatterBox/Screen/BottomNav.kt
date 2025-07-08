package com.example.ChatterBox.Screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ChatterBox.Navigation.Routes
import com.example.ChatterBox.model.BottomNavitem
import com.example.ChatterBox.viewmodel.AuthState
import com.example.ChatterBox.viewmodel.AuthViewModel

@Composable
fun BottomNav(navController: NavHostController,authViewModel: AuthViewModel){
    val navController1:NavHostController = rememberNavController()
    Scaffold(bottomBar = {
        MyBottombar(navController1,authViewModel)
    }){
        innerPadding ->
        NavHost(navController = navController1, startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)) {
            composable(Routes.Splash.routes){
                Splash(navController1,authViewModel)
            }
            composable(Routes.Register.routes){
                Splash(navController1,authViewModel)
            }
            composable(Routes.Home.routes){
                Home(navController)
            }
            composable(Routes.Login.routes){
                login(navController,authViewModel)
            }
            composable(Routes.Notification.routes){
                Notification()
            }
            composable(Routes.AddThread.routes){
                AddThread(navController1)
            }
            composable(Routes.Profile.routes){
                Profile(navController1,authViewModel)
            }
            composable(Routes.Search.routes) {
                Search(navController)
            }
            composable(Routes.BottomNav.routes){
                BottomNav(navController1,authViewModel)
            }
        }
    }

}
@Composable
fun MyBottombar(navController1: NavHostController,authViewModel: AuthViewModel) {
    val backStackEntry = navController1.currentBackStackEntryAsState()
    val authState = authViewModel.authState.observeAsState()
    if (authState.value is AuthState.Unauthenticated) {
        return // bye-bye nav bar 👋
    }
    val list =listOf(
        BottomNavitem(
            "home",
            Routes.Home.routes,
            Icons.Rounded.Home
        ),
        BottomNavitem(
            "Search",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),
        BottomNavitem(
            "Add Threads",
            Routes.AddThread.routes,
            Icons.Rounded.Add
        ),BottomNavitem(
            "Notification",
            Routes.Notification.routes,
            Icons.Rounded.Notifications
        ),
        BottomNavitem(
            "Profile",
            Routes.Profile.routes,
            Icons.Rounded.Person
        )
    )
    BottomAppBar {
        list.forEach {
            val selected:Boolean=it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(selected = selected, onClick = { navController1.navigate(it.route){
                popUpTo(navController1.graph.findStartDestination().id){
                    saveState=true
                }
                launchSingleTop=true
            } }, icon = {
                Icon(imageVector = it.icon, contentDescription = null)
            })
        }
    }
}
