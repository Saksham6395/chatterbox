package com.example.ChatterBox.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ChatterBox.model.ThreadModel
import com.example.ChatterBox.model.UserModel

@Composable
fun ThreadItem(thread: ThreadModel,
               users:UserModel,
               navController: NavController,
               userId:String,
               fromProfileScreen: Boolean = false) {
    Column {
        ConstraintLayout(modifier = Modifier.padding(16.dp)) {
            val (userImage,title,username,time,image)=createRefs()
            Image(painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = null,modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable(enabled = thread.uid != null && thread.uid.isNotEmpty() && !fromProfileScreen) {
                        thread.uid?.let {
                            navController.navigate("other_users/$it")
                        }
                    },
                contentScale = ContentScale.Crop)

            Text(text = users.username, style = TextStyle(fontSize = 20.sp), modifier = Modifier
                .constrainAs(username) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 5.dp)
                }
            )

            Text(text = thread.timestamp,modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(username.top)
                    end.linkTo(parent.end, margin = 12.dp)
                }
                , fontWeight = FontWeight.ExtraBold,
                style = TextStyle(fontSize = 7.sp),
                maxLines = 2
            )

            Text(text = thread.thread,modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(userImage.bottom)
                    start.linkTo(parent.start)
                }
                .fillMaxWidth()
            )
            if(thread.imageUrl!=""){
                Card(modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(title.bottom,margin = 8.dp)
                        start.linkTo(title.start)
                }) {
                Image(painter = rememberAsyncImagePainter(model = thread.imageUrl),
                    contentDescription = null,modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop)
                }
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}