package com.example.mentalhealthapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mentalhealthapp.databinding.ActivityMainBinding
import com.example.mentalhealthapp.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottom_nav:BottomNavigationView
    private var mCurrentMood=0
    private var mCurrentMoodIntensity=1
    val moods= arrayOf<String>(  // Depressed = 1, Sad = 2, Angry = 3, Scared = 4, Moderate = 5, Happy = 6
        "Depressed", "Sad", "Angry", "Scared", "Moderate", "Happy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        bottom_nav=binding.bottomNavView
         val navController = this.findNavController(R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(bottom_nav,navController)
        //setupBottomNavigation()

    }

    private fun setupBottomNavigation()
    {
        val resumingFromNotification=intent.getBooleanExtra(Constants.MOOD,false)
        if(resumingFromNotification)
        {
            //Toast.makeText(this,"From Notification",Toast.LENGTH_LONG).show()
            showCurrentMood()
        }


    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
        return true
    }

    private fun checkUser() {

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
        } else {
            var login: Intent= Intent(this,LoginActivity::class.java)
            startActivity(login)
            finish()
            // No user is signed in
        }
    }

    fun showCurrentMood()
    {


        val alertDialogBuilder : AlertDialog.Builder= AlertDialog.Builder(this,R.style.DarkAlertDialog)
        alertDialogBuilder.setTitle("How are you feeling Now?")
        alertDialogBuilder.setSingleChoiceItems(moods,0,{_,which->mCurrentMood = which  })
        alertDialogBuilder.setPositiveButton("Next",{_,which->showIntenstyDialog()})
        alertDialogBuilder.show()
    }
    private fun showIntenstyDialog()
    {
        val builder : AlertDialog.Builder= AlertDialog.Builder(this,R.style.DarkAlertDialog)
        val seekBar:SeekBar=SeekBar(this)
        seekBar.max=4
        seekBar.setOnSeekBarChangeListener(this);
        builder.setTitle("How is Intense is the feeling")
        builder.setView(seekBar)
        builder.setPositiveButton("save",{_,which->saveMood()})
        builder.show()

    }

    private fun saveMood()
    {
        Toast.makeText(this,"Your mood is ${moods[mCurrentMood]}, the intense of mood is ${mCurrentMoodIntensity}. Database not yet implemented",Toast.LENGTH_LONG).show()


    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean)
    {
        mCurrentMoodIntensity=p1+1


    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }


}