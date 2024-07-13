package com.example.taxinfo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.taxinfo.databinding.ActivityRegisterBinding
import com.example.taxinfo.modelClass.UserData
import com.example.taxinfo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.alreadyLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.register.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val password = binding.password.text.toString()

            val user = UserData(name, email, phone, "")

            if (name.isNotEmpty() && email.isNotEmpty()
                && phone.isNotEmpty() && password.isNotEmpty()){

                viewModel.registerUser(email, password, user, this)
                startActivity(Intent(this, MainActivity::class.java))
//                firebaseAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener {task->
//                        if (task.isSuccessful) {
//                            firebaseFirestore.collection("Users")
//                                .document(email)
//                                .set((UserData(name, email, phone, id)))
//
//                            firebaseFirestore.collection("Users")
//                                .document(email)
//                                .get().addOnSuccessListener { snapshot ->
//                                    Log.d("UserTag", "${snapshot?.get("name")}")
//                                }
//                            startActivity(Intent(this, LoginActivity::class.java))
//
//                        } else Toast.makeText(this, task.exception?.localizedMessage.toString()
//                            ,Toast.LENGTH_SHORT).show()
//                }

            } else {
                Toast.makeText(this, "All Fields Required!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}