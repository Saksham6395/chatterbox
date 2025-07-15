package com.example.ChatterBox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.ChatterBox.Navigation.NavGraph
import com.example.ChatterBox.ui.theme.ThreadAppTheme
import com.example.ChatterBox.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
        enableEdgeToEdge()

        setContent {
            ThreadAppTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel by viewModels()
                NavGraph(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}
