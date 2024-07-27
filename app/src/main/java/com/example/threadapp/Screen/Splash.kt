package com.example.threadapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.threadapp.Navigation.Routes
import com.example.threadapp.R
import com.example.threadapp.viewmodel.AuthState
import com.example.threadapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavController, authViewModel: AuthViewModel){
    val authState by authViewModel.authState.observeAsState()
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(id = R.drawable.letter_s__thread), contentDescription = "threads")
    }
    LaunchedEffect(true) {
        delay(3000)
        when (authState) {
            is AuthState.Authenticated ->
                navController.navigate(Routes.BottomNav.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop=true
                }
            is AuthState.Unauthenticated -> navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop=true
            }
            else -> Unit
        }
    }
}
