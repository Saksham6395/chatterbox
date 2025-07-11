package com.example.ChatterBox.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ChatterBox.item_view.UserItem
import com.example.ChatterBox.viewmodel.UserViewModel

@Composable
fun Search(navController: NavController){
    val userViewModel: UserViewModel = viewModel()
    var search by remember { mutableStateOf("") }
    val users by userViewModel.userList.observeAsState()
    Column {
        Text(text = "Search", fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp,16.dp,16.dp,0.dp))
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
             .padding(0.dp,0.dp,0.dp,10.dp),
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) }
        )
        LazyColumn {
            if(!users.isNullOrEmpty()){
               val filterItems=users?.filter { it.username.contains(search, ignoreCase = true) }
               items(filterItems ?: emptyList()) { user ->
                  UserItem(user, navController)
               }
            }
        }
    }
}