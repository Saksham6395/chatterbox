package com.example.threadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.threadapp.Navigation.NavGraph
import com.example.threadapp.ui.theme.ThreadAppTheme
import com.example.threadapp.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThreadAppTheme {
                FirebaseApp.initializeApp(this)
                val navController= rememberNavController()
                val authViewModel:AuthViewModel by viewModels()
                NavGraph(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}
