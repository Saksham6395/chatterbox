package com.example.threadapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserViewModel:ViewModel() {
    private val db=FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")
    private var _users = MutableLiveData<List< UserModel>>()
    var userList: LiveData<List<UserModel>> = _users

    init {
        fetchUser()
    }
    private fun fetchUser() {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result= mutableSetOf<UserModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    user.let {
                            result.add(it!!)
                    }
                }
                _users.value=result.toList()
            }
            override fun onCancelled(error: DatabaseError) {
                _users.value = emptyList()
            }
        })
    }
}
