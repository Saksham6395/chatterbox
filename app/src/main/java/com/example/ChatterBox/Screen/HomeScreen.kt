package com.example.ChatterBox.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ChatterBox.R
import com.example.ChatterBox.item_view.ThreadItem
import com.example.ChatterBox.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    val threadandUser by homeViewModel.threadandUser.observeAsState()
    Column {
        Image(painter = painterResource(id = R.drawable.letter_s__thread),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .size(50.dp)
                .padding(10.dp))
        LazyColumn {
            items(threadandUser ?: emptyList()) { pairs ->
                ThreadItem(
                    thread = pairs.first,
                    users = pairs.second,
                    navController,
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
            }
        }
    }
}





