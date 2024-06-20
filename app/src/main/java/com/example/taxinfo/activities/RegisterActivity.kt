package com.example.taxinfo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taxinfo.databinding.ActivityRegisterBinding
import com.example.taxinfo.modelClass.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

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
            val id = firebaseAuth.currentUser?.uid.toString()

            if (name.isNotEmpty() && email.isNotEmpty()
                && phone.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            firebaseFirestore
                                .collection("Users")
                                .add(UserData(name, email, phone,id))
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        else Toast.makeText(this, it.exception?.localizedMessage.toString()
                            ,Toast.LENGTH_SHORT).show()
                } 
            } else {
                Toast.makeText(this, "All Fields Required!!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}