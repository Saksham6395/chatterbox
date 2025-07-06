package com.example.threadapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadapp.model.NotificationModel
import com.example.threadapp.model.ThreadModel
import com.example.threadapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileViewModel:ViewModel() {
    private val db=FirebaseDatabase.getInstance()

    private val threadRef = db.getReference("threads")

    private val userRef = db.getReference("users")

    private val _users = MutableLiveData< UserModel?>()
    val users: LiveData<UserModel?> get() = _users

    private val _thread = MutableLiveData<List<ThreadModel>>(emptyList())
    val thread: LiveData<List<ThreadModel>> get() = _thread

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    private val _followActionLoading = MutableLiveData(false)
    val followActionLoading: LiveData<Boolean> get() = _followActionLoading

    private val _followerCount = MutableLiveData<Int>()
    val followerCount: LiveData<Int> get() = _followerCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> get() = _followingCount

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



    fun followUser(currentUid: String, targetUid: String) {
        val db = Firebase.firestore
        _followActionLoading.value = true

        // Update current user's "following" list
        val followingRef = db.collection("following").document(currentUid)


        // Update target user's "followers" list
        val followersRef = db.collection("followers").document(targetUid)

        followingRef.update("followingIds", FieldValue.arrayUnion(targetUid))
            .addOnSuccessListener {
                followersRef.update("followerIds", FieldValue.arrayUnion(currentUid))
                    .addOnSuccessListener {
                        _isFollowing.value = true
                        _followActionLoading.value = false
                        // Add unfollow notification
                        userRef.child(currentUid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentUser = snapshot.getValue(UserModel::class.java)
                                val username = currentUser?.name ?: "Someone"
                                val notification = NotificationModel(
                                    type = "follow",
                                    username = username,
                                    fromUid = currentUid,
                                    timestamp = System.currentTimeMillis()
                                )
                                db.collection("notifications").document(targetUid)
                                    .collection("userNotifications")
                                    .add(notification)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ProfileViewModel", "Failed to fetch user data: ${error.message}")
                            }
                        })
                    }
                    .addOnFailureListener {
                        _followActionLoading.value = false
                    }
            }.addOnFailureListener {
                _followActionLoading.value = false
            }
    }

    fun unfollowUser(currentUid: String, targetUid: String) {
        val db = Firebase.firestore
        _followActionLoading.value = true

        // Update current user's "following" list
        val followingRef = db.collection("following").document(currentUid)


        // Update target user's "followers" list
        val followersRef = db.collection("followers").document(targetUid)
        // Update current user's "following" list
        followingRef.update("followingIds", FieldValue.arrayRemove(targetUid))
            .addOnSuccessListener {
                followersRef.update("followerIds", FieldValue.arrayRemove(currentUid))
                    .addOnSuccessListener {
                        _isFollowing.value = false
                        _followActionLoading.value = false

                        userRef.child(currentUid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentUser = snapshot.getValue(UserModel::class.java)
                                val username = currentUser?.name ?: "Someone"
                                val notification = NotificationModel(
                                    type = "unfollow",
                                    username = username,
                                    fromUid = currentUid,
                                    timestamp = System.currentTimeMillis()
                                )
                                db.collection("notifications").document(targetUid)
                                    .collection("userNotifications")
                                    .add(notification)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ProfileViewModel", "Failed to fetch user data: ${error.message}")
                            }
                        })
                    }
                    .addOnFailureListener {
                        _followActionLoading.value = false
                    }
            }.addOnFailureListener {
                _followActionLoading.value = false
            }
    }




    fun checkIfFollowing(currentUid: String, targetUid: String) {
        val db = Firebase.firestore
        val followingRef = db.collection("following").document(currentUid)

        followingRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val followingIds = document.get("followingIds") as? List<*>
                    _isFollowing.value = followingIds?.contains(targetUid) == true
                } else {
                    _isFollowing.value = false
                }
            }
            .addOnFailureListener {
                Log.e("ProfileViewModel", "Failed to check following: ${it.message}")
                _isFollowing.value = false
            }
    }


    fun getFollowingCount(currentUid: String) {
        val db = Firebase.firestore
        val followingRef = db.collection("following").document(currentUid)

        followingRef.addSnapshotListener { snapshot, _ ->
            val list = snapshot?.get("followingIds") as? List<*>
            _followingCount.value = list?.size ?: 0
        }
    }


    fun getFollowerCount(currentUid: String) {
        val db = Firebase.firestore
        val followerRef = db.collection("followers").document(currentUid)

        followerRef.addSnapshotListener { snapshot, _ ->
            val list = snapshot?.get("followerIds") as? List<*>
            _followerCount.value = list?.size ?: 0
        }
    }
}
