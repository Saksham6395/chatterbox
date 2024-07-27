package com.example.threadapp.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.threadapp.Navigation.Routes
import com.example.threadapp.model.UserModel

@Composable
fun UserItem(users:UserModel,navController: NavController) {
    Column {
        ConstraintLayout(modifier = Modifier.padding(10.dp,10.dp,10.dp,5.dp)
            .fillMaxWidth()
            .clickable
        {
            val routes=Routes.OtherUsers.routes.replace("{data}",users.uid)
                navController.navigate(routes)
        }) {
        val (userImage,username,name)=createRefs()
        Image(painter = rememberAsyncImagePainter(model = users.imageUrl),
            contentDescription = null,modifier = Modifier
                .constrainAs(userImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop)

        Text(text = users.username, style = TextStyle(fontSize = 20.sp), modifier = Modifier
            .constrainAs(username) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 5.dp)
            },
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        Text(text = users.name,modifier = Modifier
            .constrainAs(name) {
                top.linkTo(username.bottom)
                start.linkTo(userImage.end, margin = 5.dp)
            }
            , fontWeight = FontWeight.ExtraBold,
            style = TextStyle(fontSize = 10.sp),
            maxLines = 2
        )
        }
        Divider(color = Color.Gray, thickness = 1.dp,
            modifier = Modifier.padding(bottom = 5.dp, top = 5.dp))

    }
}