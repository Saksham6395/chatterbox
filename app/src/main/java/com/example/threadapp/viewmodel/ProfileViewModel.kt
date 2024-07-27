package com.example.threadapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class ProfileViewModel:ViewModel() {
    private val db=FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")
    private val userRef = db.getReference("users")
    private val _users = MutableLiveData< UserModel>()
    val users: LiveData<UserModel> get() = _users
    private val _thread = MutableLiveData<List<ThreadModel>>()
    val thread: LiveData<List<ThreadModel>> get() = _thread

     fun fetchUsers(uid:String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user= snapshot.getValue(UserModel::class.java)
                if (user != null) {
                    _users.postValue(user)
                } else {
                    Log.e("ProfileViewModel", "User data is null for uid: $uid")
                    // Handle the case where user data is null
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileViewModel", "Failed to fetch user data: ${error.message}")
            }

        })
    }
    fun fetchThreads(uid:String) {
        threadRef.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList= snapshot.children.mapNotNull{
                    it.getValue(ThreadModel::class.java)
                }
                _thread.postValue(threadList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileViewModel", "Failed to fetch threads: ${error.message}")

            }

        })
    }

}
