package com.example.ChatterBox.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ChatterBox.model.UserModel
import com.example.ChatterBox.utils.sharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class AuthViewModel:ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>().apply { value = AuthState.Unauthenticated }
    val authState: LiveData<AuthState> = _authState

    private val userRef = FirebaseDatabase.getInstance().getReference("users")
    private val _firebaseUser = MutableLiveData<FirebaseUser>()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus(){
        val currentUser = auth.currentUser
        Log.d("AuthViewModel", "Current user: $currentUser")
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("email or password can not be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    getData(auth.currentUser?.uid,context)
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"something went wrong")
                }
            }
    }

    private fun getData(uid: String?, context: Context) {

        userRef.child(uid!!).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val userData: UserModel? = snapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    sharedPref.storeData(
                        userData.name,
                        userData.email,
                        userData.password,
                        userData.username,
                        userData.dob,
                        userData.imageUrl,
                        context
                    )
                }
                else{
                    Log.e("AuthViewModel", "Failed to read user data: ${task.exception?.message}")
                }
            }
        }
    }
    fun register(
        name:String,
        email: String,
        password: String,
        username:String,
        dob:String,
        imageUri: Uri,
        context: Context
    ) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.value=auth.currentUser
                    saveImage(name, email,password, username ,dob, imageUri, auth.currentUser?.uid,context)
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"something went wrong")
                }
            }
    }

    private fun saveImage(
        name: String,
        email: String,
        password: String,
        username: String,
        dob: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)
        if (inputStream == null) {
            _authState.postValue(AuthState.Error("Couldn't open image file"))
            return
        }

        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", tempFile.name, RequestBody.create("image/*".toMediaTypeOrNull(), tempFile))
            .addFormDataPart("upload_preset", "unsigned_android") // 👈 your preset
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/di5oxicka/image/upload")
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    CoroutineScope(Dispatchers.Main).launch {
                        _authState.value = AuthState.Error("Image upload failed: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        CoroutineScope(Dispatchers.Main).launch {
                            _authState.value = AuthState.Error("Image upload failed: ${response.message}")
                        }
                        return
                    }

                    val responseData = response.body?.string()
                    val imageUrl = Regex("\"url\":\"(.*?)\"").find(responseData ?: "")?.groups?.get(1)?.value
                        ?.replace("\\/", "/") ?: ""

                    CoroutineScope(Dispatchers.Main).launch {
                        saveData(name, email, password, username, dob, imageUrl, uid, context)
                    }
                }
            })
        }
    }


    private fun saveData(
        name: String,
        email: String,
        password: String,
        username: String,
        dob: String,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {

        val fireStored=Firebase.firestore
        val documentId = uid!!
        val followersRef=fireStored.collection("followers").document(documentId)
        val followingRef=fireStored.collection("following").document(documentId)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))

        val userData = UserModel(name,email,password,username,dob,imageUrl,uid!!)

        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                if(imageUrl.isNotEmpty()){
                    sharedPref.storeData(name, email,password, username, dob, imageUrl, context)
                }
                checkAuthStatus()
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener {
                _authState.value = AuthState.Unauthenticated
                Log.e("FirebaseSave", "Failed to save user data: ${it.message}")
                Toast.makeText(context, "Failed to save user info", Toast.LENGTH_SHORT).show()
            }
    }

    fun signout(context: Context){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        sharedPref.clear(context)
    }
}

sealed class AuthState {
    data object Loading : AuthState()
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}