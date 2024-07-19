package com.example.taxinfo.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.modelClass.UserData
import com.example.taxinfo.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel(val database : FirebaseFirestore = FirebaseFirestore.getInstance()
                    , val auth: FirebaseAuth = FirebaseAuth.getInstance()) : ViewModel() {

    fun loginUser(email : String, password : String, context: Context) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun registerUser(email: String, password: String, user: UserData, context: Context){
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
    fun logout(){
        //repo.logout(context)
        auth.signOut()
    }
    fun updateUserInfo(user: UserData, context: Context){
        database.collection("UserInfo").document(user.id).set(user)
    }

    private var _user = MutableLiveData<UserData>()
    val user : LiveData<UserData> get() = _user
    fun getUserData(id : String) {
        database.collection("UserInfo").document(id).get().addOnSuccessListener {
            val email = it.get("email").toString()
            val name = it.get("name").toString()
            val phone = it.get("phone").toString()

            _user.value = UserData(name, email, phone, id)

        }


    }
}