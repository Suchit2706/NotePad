package com.example.notepad.adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity.NO_GRAVITY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.NewNoteActivity
import com.example.notepad.R
import com.example.notepad.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeRecyclerAdapter (var context: Context, var itemList: MutableList<Note>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.txtMessage)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): HomeViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.txtMessage.text = itemList[position].message
        holder.txtDate.text = itemList[position].date
        holder.cardView.setOnLongClickListener {
            val popupMenu = PopupMenu(context,it,NO_GRAVITY,0,R.style.PopUpTheme)
            popupMenu.inflate(R.menu.menu_list)
            popupMenu.setOnMenuItemClickListener {menu->
                if (menu.itemId==R.id.delete){
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()
                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val userId = auth.uid

                    if (userId != null) {
                        db.collection("users").document(userId)
                            .collection("notes").whereEqualTo("date",itemList[position].date).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                                System.out.println(querySnapshot)
                                querySnapshot!!.forEach {doc->
                                    System.out.println(doc)
                                    doc.reference.delete().addOnSuccessListener {
                                        Toast.makeText(context,"Note deleted",Toast.LENGTH_SHORT).show()
                                        itemList.removeAt(position)
                                        notifyDataSetChanged()
                                    }.addOnFailureListener {
                                        Toast.makeText(context,"Error While Deleting",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                    }else{
                        Toast.makeText(context,"User ID null",Toast.LENGTH_SHORT).show()
                    }
//                    val intent = Intent(context, MainActivity::class.java)
//                    context.startActivity(intent)
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
            true
        }
        holder.cardView.setOnClickListener {
            val intent = Intent(context, NewNoteActivity::class.java)
            intent.putExtra("Edit",true)
            intent.putExtra("Date", itemList[position].date)
            intent.putExtra("Note", itemList[position].message)
            context.startActivity(intent)
        }
    }
}