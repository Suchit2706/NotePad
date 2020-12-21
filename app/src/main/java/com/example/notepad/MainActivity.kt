package com.example.notepad

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.notepad.adapter.ViewPagerAdapter
import com.example.notepad.fragments.HomeFragment
import com.example.notepad.fragments.ToDoListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var btnAdd : Button

    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        toolbar.popupTheme = R.style.RoundedMenu
        btnAdd = findViewById(R.id.btnAdd)
        setUpToolbar()

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(ToDoListFragment())
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            when(position){
                0->{
                    tab.text = "Grid View"
                    tab.setIcon(R.drawable.ic_grid)
                }
                1->{
                    tab.text = "List View"
                    tab.setIcon(R.drawable.ic_list)
                }
            }
        }.attach()
        tabLayout.getTabAt(0)?.icon!!.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN)

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                when(p0!!.position){
                    0->{
                        p0.icon!!.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)
                    }
                    1->{
                        p0.icon!!.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)
                    }
                }

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                try{
                    when(p0!!.position){
                        0->{
                            p0.icon!!.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN)
                        }
                        1->{
                            p0.icon!!.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN)
                        }
                    }
                }catch (e: Exception){
                    System.out.println(e)
                }
            }

        })

        btnAdd.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            intent.putExtra("Edit",false)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        finish()
        onStop()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logOut){
            auth.signOut()
            Toast.makeText(this,"Logged Out Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
    }

}