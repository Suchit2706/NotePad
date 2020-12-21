package com.example.notepad

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.notepad.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var txtTime: TextView
    lateinit var etNote: EditText
    lateinit var btnDone: Button

    lateinit var auth: FirebaseAuth

    val calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)


        txtTime = findViewById(R.id.txtTime)
        etNote = findViewById(R.id.etNote)
        btnDone = findViewById(R.id.btnFinish)

        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser!!.uid

        val time = calendar.time.toString().split(" ")
        val timeDisplay = time[2]+" "+time[1] + " "+ time[5] + " " + time[3]
        val check = intent.extras!!.getBoolean("Edit",false)
        if (check){
            txtTime.text = intent.extras!!.getString("Date","")
            etNote.setText(intent.extras!!.getString("Note",""))
        }else{
            txtTime.text = timeDisplay
        }
        etNote.requestFocus()
        btnDone.setOnClickListener {
            val message = etNote.text.toString()
            val note = Note(message, timeDisplay)
            System.out.println(note)
            if (!message.isEmpty()){
                if (check){
                    db.collection("users").document(userId)
                        .collection("notes").whereEqualTo("date",txtTime.text.toString()).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            System.out.println(querySnapshot)
                            querySnapshot!!.forEach {doc->
                                doc.reference.delete().addOnSuccessListener {
                                    System.out.println("Note deleted")
                                }.addOnFailureListener {
                                    System.out.println("Error in deleting note")
                                }
                            }
                        }
                    db.collection("users").document(userId)
                        .collection("notes").add(note).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this, "Note updated successfully",Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                        }
                }else{
                    db.collection("users").document(userId)
                        .collection("notes").add(note).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this, "Note added successfully",Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                        }
                }
            }else{
                Toast.makeText(this,"Cannot save an Empty Note",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title=""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}