package com.example.ChatterBox.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ChatterBox.viewmodel.NotificationViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Notification(viewModel: NotificationViewModel = viewModel()){
    val notifications = viewModel.notifications.observeAsState(emptyList()).value
    val context = LocalContext.current

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(uid) {
        uid?.let {
            viewModel.fetchNotifications(it)
        }
    }
    Column {
        Text(
            text = "Notifications", fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
        )
        LazyColumn {
            items(notifications) { notification ->
                val actionText = when (notification.type) {
                    "follow" -> "followed you"
                    "unfollow" -> "unfollowed you"
                    else -> "did something"
                }
                val timeAgo = getTimeAgo(notification.timestamp)

                Text(
                    text = "${notification.username} $actionText · $timeAgo",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                )
                Divider(color = Color.LightGray)
            }
        }
    }
}
fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hr ago"
        days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
        else -> "a while ago"
    }
}
