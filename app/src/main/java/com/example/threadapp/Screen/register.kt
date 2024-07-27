package com.example.threadapp.Screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.Navigation.Routes
import com.example.threadapp.R
import com.example.threadapp.viewmodel.AuthState
import com.example.threadapp.viewmodel.AuthViewModel

@Composable
fun register(navController: NavHostController,authViewModel: AuthViewModel){
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmpass  by remember { mutableStateOf("") }
    val context = LocalContext.current
//    val authViewModel :AuthViewModel= viewModel()
    val authState= authViewModel.authState.observeAsState()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Authenticated -> {
                navController.navigate(Routes.BottomNav.routes){
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context,
                    (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    //to get imageUri
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
            uri: Uri? ->
        imageUri=uri
    }
    //to get permission for different android versions
    val permissionToRequest = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else{
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    //to get permission
    val permissionLauncher=
    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){
        isGranted:Boolean->
        if(isGranted){
            launcher.launch("image/*")
        }
    }
    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = "Register Here", fontSize = 20.sp,color = Color.Black, fontWeight = FontWeight.ExtraBold)
        Image(painter = if(imageUri==null) painterResource(id = R.drawable.letter_s__thread)
        else rememberAsyncImagePainter(model = imageUri),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context,
                        permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED
                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                })
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 30.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter Username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Your Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Enter Your D.O.B") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Enter Your password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp, 5.dp, 0.dp),
            contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = confirmpass,
                onValueChange = { confirmpass = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.Center){
            ElevatedButton(onClick = {
                     if(name.isEmpty() || username.isEmpty() || email.isEmpty() || dob.isEmpty() || pass.isEmpty() || confirmpass.isEmpty() || imageUri==null) {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                     }
                    else{
                    authViewModel.register(name.toString() ,email.toString(), pass.toString(),username.toString(),dob.toString(), imageUri!!,context)
                }
                }, modifier = Modifier) {
                Text(text = "Register", fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp, modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }
        }
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center){
            TextButton(onClick = {
                navController.navigate(Routes.Login.routes){
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop=true
                } },modifier = Modifier) {
                Text(text="Already Registered? Login here",fontSize = 15.sp,
                    color= Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }
        }
    }
}