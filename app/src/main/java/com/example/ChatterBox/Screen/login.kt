package com.example.ChatterBox.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ChatterBox.Navigation.Routes
import com.example.ChatterBox.viewmodel.AuthState
import com.example.ChatterBox.viewmodel.AuthViewModel

@Composable
fun login( navController: NavHostController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Authenticated -> {
                navController.navigate(Routes.BottomNav.routes)
            }
            is AuthState.Error -> {
                Toast.makeText(context,
                    (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    Box (modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column{
            Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center){
                Text(text = "Login", fontSize = 24.sp,color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
            Box(modifier = Modifier.fillMaxWidth()
                .padding(5.dp,30.dp,5.dp,0.dp),
                contentAlignment = Alignment.Center){
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )}
            Box(modifier = Modifier.fillMaxWidth()
                .padding(5.dp,10.dp,5.dp,0.dp),
                contentAlignment = Alignment.Center){
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Enter Password")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
            Box(modifier = Modifier.fillMaxWidth()
                .padding(0.dp,20.dp,0.dp,0.dp),
                contentAlignment = Alignment.Center){
                ElevatedButton(onClick = {
                    if(email.isEmpty() || pass.isEmpty()){
                        Toast.makeText(context,"please provide all fields",Toast.LENGTH_SHORT).show()
                    }
                    authViewModel.login(email,pass,context) // called authViewmodel class login function
                },modifier = Modifier) {
                Text(text = "Login", fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp, modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
                }
            }
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){
                TextButton(onClick = {
                    navController.navigate(Routes.Register.routes){
                        popUpTo(navController.graph.startDestinationId)
                    }
                },modifier = Modifier) {
                Text(text="New User?Create Account",fontSize = 15.sp,
                    color= Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
                }
            }
        }
        if (authState.value is AuthState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {}, // makes background untouchable
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

    }
}


