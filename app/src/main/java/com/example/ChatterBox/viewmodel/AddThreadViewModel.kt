package com.example.ChatterBox.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ChatterBox.model.ThreadModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddThreadViewModel : ViewModel() {

    private val userRef = FirebaseDatabase.getInstance().getReference("threads")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> get() = _isPosted

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val client = OkHttpClient()

    fun uploadAndPostThread(
        imageUri: Uri,
        context: Context,
        userId: String,
        threadText: String
    ) {
        _isLoading.value = true
        uploadToCloudinary(
            imageUri,
            context,
            onSuccess = { secureUrl ->
                saveData(userId, threadText, secureUrl)
            },
            onFailure = { e ->
                _isLoading.postValue(false)
                _isPosted.postValue(false)
                _errorMessage.postValue("Upload failed: ${e.message}")
            }
        )
    }

    private fun uploadToCloudinary(
        imageUri: Uri,
        context: Context,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val cloudName = "di5oxicka"
        val uploadPreset = "unsigned_android"
        val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri) ?: run {
            onFailure(Exception("Unable to open image URI"))
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("upload_preset", uploadPreset)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), inputStream.readBytes())
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody ?: "")
                    val secureUrl = json.getString("secure_url")
                    onSuccess(secureUrl)
                } else {
                    onFailure(Exception("Upload failed: ${response.message}"))
                }
            }
        })
    }

    fun saveData(userId: String, threads: String, imageUri: String) {
        val currentDateTime = LocalDateTime.now(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\n HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _isLoading.value = false
            _isPosted.value = false
            _errorMessage.value = "User not authenticated"
            return
        }

        val threadData = ThreadModel(userId, threads, imageUri, formattedDateTime.toString())
        val key = userRef.push().key ?: run {
            _isLoading.value = false
            _isPosted.value = false
            _errorMessage.value = "Failed to generate thread key"
            return
        }

        userRef.child(key).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.value = true
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isPosted.value = false
                _isLoading.value = false
                _errorMessage.value = "Database error: ${it.message}"
            }
    }

    fun resetPostState() {
        _isPosted.value = false
        _errorMessage.value = null
    }
}
