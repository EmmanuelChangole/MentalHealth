package com.example.mentalhealthapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.mentalhealthapp.database.mood.Mood
import com.example.mentalhealthapp.database.mood.MoodDatabase
import com.example.mentalhealthapp.databinding.ActivityMainBinding
import com.example.mentalhealthapp.model.User
import com.example.mentalhealthapp.utils.Constants
import com.example.mentalhealthapp.viewmodel.MoodViewModel
import com.example.mentalhealthapp.viewmodel.MoodViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottom_nav:BottomNavigationView
    private var mCurrentMood=0
    private var mCurrentMoodIntensity=1
    val moods= arrayOf<String>(  // Depressed = 1, Sad = 2, Angry = 3, Scared = 4, Moderate = 5, Happy = 6
        "Depressed", "Sad", "Angry", "Scared", "Moderate", "Happy")
    private lateinit var headerView: View
    private lateinit var navigationView: NavigationView
    private lateinit var tvProfileName: TextView
    private lateinit var imgVeiew:CircleImageView
    lateinit var filename:String
    var imageUri:String?=null
    var uri: Uri?=null
    var inputData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkUser()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        bottom_nav=binding.bottomNavView
         val navController = this.findNavController(R.id.nav_host_fragment)
        navigationView=binding.navView
        headerView=navigationView.getHeaderView(0)

        tvProfileName=headerView.findViewById(R.id.edUsername)
        imgVeiew=headerView.findViewById(R.id.imageView)


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
        checkUser()
       // setupBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
        return true
    }



    private fun checkUser() {

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
        {
            val mRef = FirebaseDatabase.getInstance("https://decent-envoy-323808-default-rtdb.firebaseio.com/").getReference("users").child(user.uid)
            val eventListener: ValueEventListener =object : ValueEventListener
            {
                @SuppressLint("RestrictedApi")
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    if (snapshot.exists())
                    {
                        var user = snapshot.getValue(User::class.java)
                        tvProfileName.setText(user!!.username)

                        val storageRef = FirebaseStorage.getInstance().getReference()
                        filename=FirebaseAuth.getInstance().currentUser!!.uid.toString()+ ".jpg"
                        val pathStrorage=storageRef.child("images/$filename")
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        if(pathStrorage!=null)
                        {
                            pathStrorage.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                               Glide.with(this@MainActivity)
                                   .load(it)
                                   .placeholder(R.drawable.ic_person)
                                   .into(imgVeiew)


                            }
                        }



                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            mRef.addListenerForSingleValueEvent(eventListener)

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
        val time = System.currentTimeMillis()/10000
        val mood=Mood(
            title = moods[mCurrentMood],
            description = "the intense of mood is ${mCurrentMoodIntensity}",
            date = mCurrentMood+1,
            value =mCurrentMoodIntensity)
        val dataSource = MoodDatabase.getInstance(application).moodDao
        val viewModelFactory = MoodViewModelFactory(dataSource, application)
        val moodViewModel=ViewModelProvider(this,viewModelFactory).get(MoodViewModel::class.java)
        moodViewModel.addMood(mood)

      //  Toast.makeText(this,"Your mood is ${moods[mCurrentMood]}, the intense of mood is ${mCurrentMoodIntensity}. Database not yet implemented",Toast.LENGTH_LONG).show()


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