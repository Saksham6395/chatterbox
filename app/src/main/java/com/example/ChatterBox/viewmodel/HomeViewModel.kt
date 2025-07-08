package com.example.ChatterBox.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ChatterBox.model.ThreadModel
import com.example.ChatterBox.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeViewModel:ViewModel() {
    private val db=FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")
    private var _threadandUser = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    var threadandUser: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadandUser

    init {
        fetchThreadsAndUsers {
            _threadandUser.value=it
        }
    }
    private fun fetchThreadsAndUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        threadRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeViewModel", "Failed to fetch threads : ${error.message}")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val result= mutableSetOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUsersFromThread(it!!){
                            user->
                            result.add(Pair(it,user))

                            if(result.size==snapshot.childrenCount.toInt()){
                                val sortedResult = result.toList().sortedByDescending { pair ->
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\n HH:mm:ss")
                                    LocalDateTime.parse(pair.first.timestamp, formatter)
                                }
                                onResult(sortedResult)
                            }
                        }
                    }
                }
            }
        })
    }
    fun fetchUsersFromThread(thread: ThreadModel,onResult: (UserModel) -> Unit){
        db.getReference("users").child(thread.uid)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                user?.let(onResult)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeViewModel", "Failed to fetch threads : ${error.message}")
            }
        } )
    }
}
