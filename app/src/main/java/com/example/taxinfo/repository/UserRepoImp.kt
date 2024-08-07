package com.example.taxinfo.repository

import android.content.Context
import android.widget.Toast
import com.example.taxinfo.modelClass.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class UserRepoImp(val database : FirebaseFirestore = FirebaseFirestore.getInstance()
                  , val auth: FirebaseAuth = FirebaseAuth.getInstance()) : UserRepo {

    override fun loginUser(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun registerUser(email: String, password: String, user: UserData, context: Context) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                user.id = it.result.user?.uid.toString()
                updateUserInfo(user, context)
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    throw it.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    Toast.makeText(context, "Authentication failed, Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Authentication failed, Invalid email entered", Toast.LENGTH_SHORT).show()
                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(context, "Authentication failed, Email already registered.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun logout(context: Context) {
        auth.signOut()
    }

    override fun updateUserInfo(user: UserData, context: Context) {
        database.collection("UserInfo").document(user.id).set(user)
    }

    override fun getUserData(id: String) : String {
        var userEmail = ""
        database.collection("UserInfo").document(id).get().addOnSuccessListener {
            userEmail = it.get("email").toString()
        }
        return userEmail
    }

}