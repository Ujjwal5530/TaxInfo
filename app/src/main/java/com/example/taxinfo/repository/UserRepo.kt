package com.example.taxinfo.repository

import android.content.Context
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.modelClass.UserData
import com.google.firebase.firestore.auth.User

interface UserRepo {
    fun loginUser(email : String, password : String, context: Context)
    fun registerUser(email: String, password: String, user: UserData,context: Context)
    fun logout(context: Context)
    fun updateUserInfo(user: UserData,context: Context)
    fun getUserData(id : String) : String
}