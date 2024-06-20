package com.example.taxinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taxinfo.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.alreadyLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.register.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val password = binding.password.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()
                && phone.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) startActivity(Intent(this, LoginActivity::class.java))
                        else Toast.makeText(this, it.exception?.localizedMessage.toString()
                            ,Toast.LENGTH_SHORT).show()
                } 
            } else {
                Toast.makeText(this, "All Fields Required!!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}