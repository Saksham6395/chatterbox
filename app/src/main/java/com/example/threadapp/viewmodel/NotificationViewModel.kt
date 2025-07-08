package com.example.threadapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.NotificationModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationViewModel : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationModel>>()
    val notifications: LiveData<List<NotificationModel>> get() = _notifications

    private val db = Firebase.firestore

    fun fetchNotifications(uid: String) {
        db.collection("notifications")
            .document(uid)
            .collection("userNotifications")
            .orderBy("timestamp", Query.Direction.DESCENDING) // optional: to order by time
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Log or handle error
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { it.toObject(NotificationModel::class.java) }
                _notifications.value = list ?: emptyList()
            }
    }


}
