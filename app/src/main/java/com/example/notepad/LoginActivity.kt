package com.example.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var txtSignUp: TextView
    lateinit var txtSignUpGoogle: TextView
    lateinit var btnLogin: Button
    lateinit var auth: FirebaseAuth
    lateinit var progressLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        txtSignUpGoogle = findViewById(R.id.txtSignUpGoogle)
        btnLogin = findViewById(R.id.btnLogin)
        progressLayout = findViewById(R.id.progressLayout)

        txtSignUpGoogle.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            progressLayout.visibility = View.GONE
        }


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Invalid Entry",Toast.LENGTH_SHORT).show()
            }else{
                loginUser(email, password)
            }
        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        if(auth.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onResume()
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task->
            if (task.isSuccessful){
                Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Invalid Entry",Toast.LENGTH_SHORT).show()
            }
        }
    }

}