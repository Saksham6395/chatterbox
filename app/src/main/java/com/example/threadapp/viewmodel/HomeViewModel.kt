package com.example.threadapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



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
            override fun onDataChange(snapshot: DataSnapshot) {
                val result= mutableSetOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUsersFromThread(it!!){
                            user->
                            result.add(Pair(it,user))

                            if(result.size==snapshot.childrenCount.toInt()){
                             onResult(result.toList())
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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

                    }
                } )
    }
}
