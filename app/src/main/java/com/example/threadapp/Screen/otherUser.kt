package com.example.threadapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.item_view.ThreadItem
import com.example.threadapp.utils.sharedPref
import com.example.threadapp.viewmodel.AuthState
import com.example.threadapp.viewmodel.AuthViewModel
import com.example.threadapp.viewmodel.ProfileViewModel

@Composable
fun OtherUserProfile(navController: NavHostController, authViewModel: AuthViewModel, uid: String?){
    val authState=authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel()
    val threads by profileViewModel.thread.observeAsState()
    val users by profileViewModel.users.observeAsState()
    LaunchedEffect(uid) {
        try {
            uid?.let {
                profileViewModel.fetchThreads(it)
                profileViewModel.fetchUsers(it)
            } ?: throw IllegalArgumentException("User ID is null")
        } catch (e: Exception) {
            println("Error fetching data: ${e.localizedMessage}")
        }
    }
    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Unauthenticated ->navController.navigate("login")
            else -> Unit
        }
    }
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(
        WindowInsets.safeContent
            .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
            .asPaddingValues())) {
       item{
           if (users != null) {
           ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
               val (
                   userImage,
                   username,
                   name,
                   logout,
                   follower,
                   following
               ) = createRefs()
               Image(painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                   contentDescription = null, modifier = Modifier
                       .constrainAs(userImage) {
                           top.linkTo(parent.top)
                           end.linkTo(parent.end)
                       }
                       .size(120.dp)
                       .clip(CircleShape),
                   contentScale = ContentScale.Crop)
               Text(text = users!!.name,
                   style = TextStyle(fontSize = 24.sp),
                   fontWeight = FontWeight.ExtraBold,
                   modifier = Modifier
                       .constrainAs(name) {
                           top.linkTo(parent.top)
                           start.linkTo(parent.start)
                       }
                       .fillMaxWidth()
               )
               Text(text = users!!.username,
                   style = TextStyle(fontSize = 20.sp),
                   modifier = Modifier
                       .constrainAs(username) {
                           top.linkTo(name.bottom)
                           start.linkTo(parent.start)
                       }
               )
               Text(text = "0  Followers", style = TextStyle(fontSize = 20.sp), modifier = Modifier
                   .constrainAs(follower) {
                       top.linkTo(username.bottom)
                       start.linkTo(parent.start)
                   }
               )
               Text(text = "0 Following", style = TextStyle(fontSize = 20.sp), modifier = Modifier
                   .constrainAs(following) {
                       top.linkTo(follower.bottom)
                       start.linkTo(parent.start)
                   }
               )
               ElevatedButton(
                   onClick = {  },
                   modifier = Modifier.constrainAs(logout) {
                       top.linkTo(following.bottom)
                       start.linkTo(parent.start)
                   }) {
                   Text(text = "Follow")
               }

           }
       }
       }

        item{
            Divider(color = Color.Gray, thickness = 1.dp)
        }

        if(users!=null && threads!=null){
          items(threads ?: emptyList()) { item ->
            ThreadItem(
                thread = item,
                users = users!!,
                navController,
                sharedPref.getUserName(context)
                )
            }
        }
    }
}