package com.example.mentalhealthapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mentalhealthapp.model.User
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream





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
    lateinit var buttonUpdate:Button
    lateinit var progressBar:ProgressBar
    lateinit var edName:EditText
    lateinit var filename:String
    var imageUri:String?=null
    var uri:Uri?=null
    var inputData: ByteArray? = null


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
        progressBar=view.findViewById(R.id.progressBar2)
        progressBar.visibility=View.GONE
        edName=view.findViewById(R.id.edUsername)
        circularImageView.setOnClickListener(View.OnClickListener
        {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })
        buttonUpdate.setOnClickListener( View.OnClickListener {
            val name=edName.text.toString()

            uploadFile(inputData,name)
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

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            mRef.addListenerForSingleValueEvent(eventListener)

        }
        val storageRef = FirebaseStorage.getInstance().getReference()
        filename=FirebaseAuth.getInstance().currentUser!!.uid.toString()+ ".jpg"
        val pathStrorage=storageRef.child("images/$filename")
        val ONE_MEGABYTE: Long = 1024 * 1024
        if(pathStrorage!=null)
        {
            pathStrorage.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                context?.applicationContext?.let { it1 ->
                    Glide.with(it1)
                        .load(it)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(circularImageView)

                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
          ///  val uri: Uri = data?.data!!
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


    fun uploadFile(uri: ByteArray?, userName:String)
    {
        val user = User(userName,filename)
        val userUid=FirebaseAuth.getInstance().currentUser?.uid
        val database=FirebaseDatabase.getInstance()
        val mRef=database.getReference()
        if (userUid != null) {
            mRef.child("users").child(userUid).setValue(user)
        }

        progressBar.visibility=View.VISIBLE
        if(uri!=null)
        {
             val filename=FirebaseAuth.getInstance().currentUser!!.uid.toString()+ ".jpg"

             val refStorage=FirebaseStorage.getInstance().reference.child("images/$filename")
             refStorage.putBytes(uri).addOnSuccessListener{
                OnSuccessListener<UploadTask.TaskSnapshot> {
                    taskSnapshot ->  taskSnapshot.storage.downloadUrl.addOnSuccessListener{
                        val uri=it.toString()
                        imageUri =uri
                        progressBar.visibility=View.GONE

                }
                }
            }.addOnFailureListener(OnFailureListener {
                print(it.message)
             }).addOnProgressListener {
                 val progress=(100.0 *it.bytesTransferred)/it.totalByteCount
                 progressBar.progress=progress.toInt()
                 Toast.makeText(context?.applicationContext,it.bytesTransferred.toString(),Toast.LENGTH_LONG).show()
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