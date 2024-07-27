package com.example.threadapp.Screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.threadapp.Navigation.Routes
import com.example.threadapp.R
import com.example.threadapp.utils.sharedPref
import com.example.threadapp.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThread(navController: NavController){
    var thread by remember { mutableStateOf("") }
    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)
    val context = LocalContext.current
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
    LaunchedEffect(key1 = isPosted){
        if(isPosted){
            thread=""
            imageUri=null
            Toast.makeText(context,"Thread Posted", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.routes){
                popUpTo(Routes.AddThread.routes){
                    inclusive=true
                }
            }
        }
    }
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        val (crossPic, title, logo,content,name, editText,button,postButton) = createRefs()
        Image(painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.AddThread.routes) {
                            inclusive = true
                        }
                    }
                }
                .size(30.dp))
        Text(text = "New Thread", fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp, modifier = Modifier.
                constrainAs(title){
                    top.linkTo(parent.top)
                    start.linkTo(crossPic.end,)
                    bottom.linkTo(crossPic.bottom) }
                ,
            textAlign = TextAlign.Center)
        Image(painter = rememberAsyncImagePainter(model = sharedPref.getImage(context)),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(title.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                }
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop)
        Text(text = sharedPref.getName(context),
        fontSize = 20.sp,
        modifier = Modifier.
            constrainAs(name){
                top.linkTo(logo.top)
                start.linkTo(logo.end,margin = 12.dp)
                bottom.linkTo(logo.bottom)}
            , textAlign = TextAlign.Center)
        BasicTextFieldWithHint(hint = "add a new thread", value = thread,
            onValueChange = {thread=it}, modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(name.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(0.dp, 4.dp, 4.dp, 4.dp)
        )
        if(imageUri==null){
            Image(painter=painterResource(id = R.drawable.baseline_attach_file_24),contentDescription = null
                , modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(editText.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }
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
                    })}
        else{
            Box(modifier = Modifier
                .size(400.dp)
                .background(color = Color.Gray)
                .padding(0.5.dp)
                .constrainAs(button)
                {
                    top.linkTo(editText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(12.dp)
                .height(250.dp)
                .fillMaxWidth()
            ){
            Image(painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = "Person", modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop)
            Icon(imageVector = Icons.Default.Close,contentDescription = "Remove image",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { imageUri = null })
                }
            Text(text = "anyone can reply",
                modifier = Modifier.constrainAs(content){
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })
            TextButton(onClick = {
                if(imageUri==null){
                    threadViewModel.saveData(FirebaseAuth.getInstance().currentUser!!.uid,thread,"")
                }else{
                    threadViewModel.saveImage(FirebaseAuth.getInstance().currentUser!!.uid,thread,imageUri!!)
                }
            },
                modifier = Modifier.constrainAs(postButton){
                    top.linkTo(content.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(content.bottom)
                }){
                Text(text = "Post")
            }
        }
    }
}
@Composable
fun BasicTextFieldWithHint(hint:String,value:String,onValueChange: (String) -> Unit, modifier: Modifier){
    Box(modifier = modifier){
        if(value.isEmpty()){
            Text(text = hint,color = Color.Gray)
        }
        BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = modifier.fillMaxWidth())
    }
}
