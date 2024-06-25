package com.example.taxinfo.repository

import com.example.taxinfo.modelClass.UserData
import com.google.firebase.firestore.FirebaseFirestore

class UserRepo {

    private lateinit var firestore: FirebaseFirestore

    fun getUserInfo() : UserData {

        var userData = UserData("", "", "", "")

        firestore = FirebaseFirestore.getInstance()


        return userData
    }
}