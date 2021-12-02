package com.example.mentalhealthapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mentalhealthapp.model.User
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Array
import java.util.HashMap
import kotlin.coroutines.Continuation


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var  circularImageView:CircleImageView
    lateinit var buttonUpdate:ImageView
    lateinit var buttonRemove:ImageView
    lateinit var progressBar: ArchedImageProgressBar
    lateinit var edName:EditText
    lateinit var filename:String
    var imageUri:String?=null
    var uri:Uri?=null
    var inputData: ByteArray? = null
    var isUpdated=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Profile"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circularImageView=view.findViewById(R.id.profile_image)
        buttonUpdate=view.findViewById(R.id.butUpdate)
        buttonRemove=view.findViewById(R.id.butRemove)
        edName=view.findViewById(R.id.edUsername)
        progressBar=view.findViewById(R.id.linkedin_progressBar)
        progressBar.setProgressText(arrayOf("Uploading"), "#c5cae9")
        progressBar.visibility=View.GONE
        buttonRemove.setOnClickListener {
            val userUid=FirebaseAuth.getInstance().currentUser?.uid
            var mRef=FirebaseDatabase.getInstance().getReference("users").child(userUid.toString())
            val map = HashMap<String, Any>()
            map.put("imageURL", ""+"")
            mRef.updateChildren(map)
            (activity as MainActivity?)?.checkUser()
            Glide.with(it)
                .load("")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person)
                .into(circularImageView)

            Toast.makeText(context, "Profile removed", Toast.LENGTH_LONG).show()
        }
        circularImageView.setOnClickListener(View.OnClickListener
        {
            ImagePicker.with(this)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })
        edName.setOnClickListener{
            edName.isCursorVisible=true
        }
        buttonUpdate.setOnClickListener( View.OnClickListener {
            val name=edName.text.toString()
            edName.isCursorVisible=false
               CoroutineScope(Dispatchers.Main).launch{
                   uploadFile(inputData,name)
               }



           // Toast.makeText(context?.applicationContext,uri.toString(),Toast.LENGTH_LONG).show();
        })

        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null)
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
                        edName.setText(user!!.username)
                        context?.applicationContext?.let {
                            Glide.with(it)
                                .load(user.imageURL)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_person)
                                .into(circularImageView)
                        }

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            mRef.addListenerForSingleValueEvent(eventListener)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
          ///  val uri: Uri = data?.data!!
              isUpdated=true
              uri=data?.data
             val iStream: InputStream = context?.applicationContext?.getContentResolver()
                ?.openInputStream(uri!!)!!
             inputData = getBytes(iStream)

                    //  upLoadFile(uri)

            // Use Uri object instead of File to avoid storage permissions
            circularImageView.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity()?.applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(getActivity()?.applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
    }

    fun uploadFile(uri: ByteArray?, userName:String)
    {
        val userUid=FirebaseAuth.getInstance().currentUser?.uid
        if (userUid != null) {

            var mRef=FirebaseDatabase.getInstance().getReference("users").child(userUid.toString())
            val eventListener: ValueEventListener =object : ValueEventListener
            {

                @SuppressLint("RestrictedApi")
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    if (snapshot.exists())
                    {
                        var user = snapshot.getValue(User::class.java)
                        if(userName.equals(user!!.username))
                        {
                            if(!isUpdated)
                            {
                                Toast.makeText(context,"There is no changes to update",Toast.LENGTH_SHORT).show()
                                return
                            }


                        }
                        else{
                            Toast.makeText(context,"Username updated",Toast.LENGTH_SHORT).show()

                        }


                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            mRef.addListenerForSingleValueEvent(eventListener)

            val map = HashMap<String, Any>()
            map.put("username", ""+userName)
            mRef.updateChildren(map)
            (activity as MainActivity?)?.checkUser()


        }



        if(uri!=null)
        {
            progressBar.visibility=View.VISIBLE
             val filename=FirebaseAuth.getInstance().currentUser!!.uid.toString()+ ".jpg"
             val refStorage=FirebaseStorage.getInstance().reference.child("images/$filename")
             val uploadTask=refStorage.putBytes(uri)
             val uriTask=uploadTask.continueWithTask { task ->
                 if (!task.isSuccessful) {
                     task.exception?.let {
                         throw it
                     }
                 }
                 refStorage.downloadUrl
             }.addOnCompleteListener{task->
                 if(task.isSuccessful)
                 {
                     progressBar.visibility=View.GONE
                     val context: Context? = context
                     if(context!=null)
                     {
                         Toast.makeText(requireContext().applicationContext,"Uploaded",Toast.LENGTH_SHORT).show()
                          this.inputData=null
                          isUpdated=false


                     }
                     

                     val downloadUrl=task.result.toString()
                    var mRef=FirebaseDatabase.getInstance().getReference("users").child(userUid.toString())
                     val map = HashMap<String, Any>()
                    /* context?.let {
                         Glide.with(it)
                             .load(uri)
                             .diskCacheStrategy(DiskCacheStrategy.ALL)
                             .placeholder(R.drawable.ic_person)
                             .into(circularImageView)
                     }*/
                     map.put("imageURL", ""+downloadUrl)
                     mRef.updateChildren(map)
                     (activity as MainActivity?)?.checkUser()

                 }
                 else{

                 }
             }




        }


    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}