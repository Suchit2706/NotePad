package com.example.notepad.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notepad.NewNoteActivity
import com.example.notepad.R
import com.example.notepad.adapter.HomeRecyclerAdapter
import com.example.notepad.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ToDoListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var auth: FirebaseAuth

    lateinit var recyclerHome: RecyclerView
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var noteComparator = Comparator<Note>{ r1, r2->
        if(r1.date.compareTo(r2.date, true)==0){
            r1.date.compareTo(r2.date, true)
        }else{
            r1.date.compareTo(r2.date, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.indeterminateDrawable.setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.MULTIPLY)


        auth = FirebaseAuth.getInstance()
        showData(view)

        return view
    }

    override fun onResume() {
        view?.let { showData(it) }
        super.onResume()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToDoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showData(view: View){
        val db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser!!.uid
        try {
            db.collection("users").document(userId)
                .collection("notes").get().addOnSuccessListener {
                    if(it!=null){
                        var notes = ArrayList<Note>()
                        for (i in it){
                            val note = Note(i.data["message"].toString(), i.data["date"].toString())
                            notes.add(note)
                        }
                        Collections.sort(notes, noteComparator)
                        notes.reverse()
                        progressLayout.visibility = View.GONE
                        recyclerHome = view.findViewById(R.id.recyclerHome)
                        layoutManager = LinearLayoutManager(context)
                        recyclerAdapter = HomeRecyclerAdapter(activity as Context, notes)
                        recyclerHome.adapter = recyclerAdapter
                        recyclerHome.layoutManager = layoutManager
                    }
                }
        }catch (e: Exception){
            System.out.println(e)
        }
    }
}