package com.example.notepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.notepad.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    lateinit var etUserName: EditText
    lateinit var etMail: EditText
    lateinit var etPass: EditText
    lateinit var etConfirmPass: EditText
    lateinit var btnSignUp: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        etUserName = findViewById(R.id.etUsername)
        etMail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPassword)
        etConfirmPass = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        auth = FirebaseAuth.getInstance()
        
        btnSignUp.setOnClickListener {
            val email = etMail.text.toString()
            val password = etPass.text.toString()
            val username = etUserName.text.toString()
            val confirmPass = etConfirmPass.text.toString()

            if (email.isEmpty()||password.isEmpty()||username.isEmpty()||confirmPass.isEmpty()){
                Toast.makeText(this, "Invalid Entry",Toast.LENGTH_SHORT).show()
            }else if (password!=confirmPass){
                Toast.makeText(this,"Invalid Password",Toast.LENGTH_SHORT).show()
            }else{
                signIn(email, password, username)
            }
        }
    }
    private fun signIn(email: String, pass: String, username: String){
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful){
                val db = FirebaseFirestore.getInstance()
                val userId = auth.currentUser!!.uid
                val u = User(username, pass, email)
                db.collection("users").document(userId)
                    .collection("profile").document("it").set(u).addOnSuccessListener {
                    Toast.makeText(this,"User added successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Error while signing up...!!", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this,"SignUp successful",Toast.LENGTH_SHORT).show()
                onBackPressed()
            }else{
                Toast.makeText(this,"Error while signing up...!!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}