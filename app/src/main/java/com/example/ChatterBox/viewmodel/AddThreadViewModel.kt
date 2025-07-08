package com.example.ChatterBox.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ChatterBox.model.ThreadModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID


class AddThreadViewModel:ViewModel() {
    private val userRef = FirebaseDatabase.getInstance().getReference("threads")
    private val storageRef = Firebase.storage.reference
    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> get() = _isPosted
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveImage(
        userId: String,
        threads: String,
        imageUri: Uri
    ) {
        _isLoading.value = true
        val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(userId, threads, it.toString())
            }.addOnFailureListener {
                _isLoading.value = false
                _isPosted.value = false
            }
        }.addOnFailureListener {
            _isLoading.value = false
            _isPosted.value = false
        }
    }

    fun saveData(
        userId: String,
        threads: String,
        imageUri: String
    ) {
        val currentDateTime = LocalDateTime.now(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\n HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val threadData = ThreadModel(userId, threads, imageUri, formattedDateTime.toString())

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.value = true
                _isLoading.value = false
            }.addOnFailureListener {
                _isPosted.value = false
                _isLoading.value = false
            }
    }

    fun resetPostState() {
        _isPosted.value = false
    }
}
