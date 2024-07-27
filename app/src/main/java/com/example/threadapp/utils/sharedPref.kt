package com.example.threadapp.utils

import android.content.Context


object sharedPref {
    fun storeData(
        name: String, email: String,password: String, username: String, dob: String,
        imageUrl: String,
        context: Context
    ){
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("username", username)
        editor.putString("dob", dob)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }
    fun getName(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("name", "")!!
    }
    fun getPassword(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("password", "")!!
    }
    fun getUserName(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("username", "")!!
    }
    fun getEmail(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("email", "")!!
    }
    fun getDob(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("dob", "")!!
    }
    fun getImage(context: Context):String{
        val sharedPref = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        return sharedPref.getString("imageUrl", "")!!
    }


}