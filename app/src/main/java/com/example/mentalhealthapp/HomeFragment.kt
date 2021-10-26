package com.example.mentalhealthapp

import android.app.AlertDialog
import android.app.NotificationManager
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mentalhealthapp.databinding.FragmentItemListBinding
import com.example.mentalhealthapp.database.mood.Mood
import com.example.mentalhealthapp.database.mood.MoodDatabase
import com.example.mentalhealthapp.utils.sendNotification
import com.example.mentalhealthapp.viewmodel.DisorderViewModel
import com.example.mentalhealthapp.viewmodel.MoodViewModel
import com.example.mentalhealthapp.viewmodel.MoodViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_item_list.*

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : Fragment() ,SeekBar.OnSeekBarChangeListener{
    private val  disorder=null
    private lateinit var disorderViewModel: DisorderViewModel
    private lateinit var moodViewModel:MoodViewModel
    private lateinit var adapter :MyItemRecyclerViewAdapter
    private lateinit var myItemRecyclerViewAdapter:MyItemRecyclerViewAdapter
    val TAG= HomeFragment::class.java.simpleName as String
    private lateinit var fab:FloatingActionButton
    private val MOOD_PARAMETER="ItemFragmentMood"
    private var mCurrentMood=0
    private var mCurrentMoodIntensity=1
    val moods= arrayOf<String>(  // Depressed = 1, Sad = 2, Angry = 3, Scared = 4, Moderate = 5, Happy = 6
        "Depressed", "Sad", "Angry", "Scared", "Moderate", "Happy")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
       // (activity as AppCompatActivity?)!!.supportActionBar?.title="Home



        retainInstance = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //navigate to the Fragment that has the same id as the selected menu item
        return NavigationUI.
        onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Home"
        val binding: FragmentItemListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_item_list, container, false)
        fab=binding.fab
        fab.setOnClickListener(View.OnClickListener
        {
            showCurrentMood()
           // Toast.makeText(this.context,"Whats on your mind? not yet implemented",Toast.LENGTH_SHORT).show()
        })
        val application = requireNotNull(this.activity).application

         /*   val dataSource = DisorderDatabase.getInstance(application).disorderDatabaseDao
        val viewModelFactory = DisorderViewModelFactory(dataSource, application)
         disorderViewModel = ViewModelProvider(
            this, viewModelFactory).get(DisorderViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.disorderViewModel=disorderViewModel
        disorderViewModel.deleteAll()
        disorderViewModel.populateData()*/

        val moodDataSource=MoodDatabase.getInstance(application).moodDao
        val viewModelFactory=MoodViewModelFactory(moodDataSource,application)
        moodViewModel=ViewModelProvider(this,viewModelFactory).get(MoodViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.moodViewModel=moodViewModel
        adapter = MyItemRecyclerViewAdapter()
        moodViewModel.getAllMoods().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if(it.isEmpty())
            {
                val tvDetails:TextView=binding.tvError
                tvDetails.visibility=View.VISIBLE
            }

        })






        return   binding.root;


    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Home"
        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            requireContext().getText(R.string.ready).toString(),
            requireContext()
        )


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.title="Home"
        myItemRecyclerViewAdapter=MyItemRecyclerViewAdapter();
        list_recycler_view.apply{
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter=myItemRecyclerViewAdapter
        }
        moodViewModel.getAllMoods().observe(viewLifecycleOwner, Observer {
            if(it.isEmpty())
            {
                val tvDetails:TextView=view.findViewById(R.id.tvError);
                tvDetails.visibility=View.VISIBLE;

            }
            else
            {
                val tvDetails:TextView=view.findViewById(R.id.tvError);
                tvDetails.visibility=View.GONE;

            }
          myItemRecyclerViewAdapter.submitList(it)


        })


        var itemTouchHelperCallback=setUpTouchHelper();
        var itemTouchHelper=ItemTouchHelper(itemTouchHelperCallback)
         itemTouchHelper.attachToRecyclerView(list_recycler_view)

    }

    private fun setUpTouchHelper(): ItemTouchHelper.Callback {
        val itemTouchHelperCallback = object: ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // Specify the directions of movement
                val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val dragFlag=ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlag, dragFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Notify your adapter that an item is moved from x position to y position
                myItemRecyclerViewAdapter.notifyItemMoved(viewHolder.adapterPosition,target.absoluteAdapterPosition)
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                // true: if you want to start dragging on long press
                // false: if you want to handle it yourself
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                val position=viewHolder.absoluteAdapterPosition
                val currentMood=myItemRecyclerViewAdapter.get_Item(position)
                moodViewModel.deleteMood(currentMood)
                myItemRecyclerViewAdapter.notifyItemRemoved(position)

            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                // Hanlde action state changes
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // Called by the ItemTouchHelper when the user interaction with an element is over and it also completed its animation
                // This is a good place to send update to your backend about changes
                


              //  moodViewModel.deleteMood()


            }
        }
       return itemTouchHelperCallback
    }

    fun showCurrentMood()
    {


        val alertDialogBuilder : AlertDialog.Builder= AlertDialog.Builder(getActivity(),R.style.DarkAlertDialog)
        alertDialogBuilder.setTitle("How are you feeling Now?")
        alertDialogBuilder.setSingleChoiceItems(moods,0,{_,which->mCurrentMood = which  })
        alertDialogBuilder.setPositiveButton("Next",{_,which->showIntenstyDialog()})
        alertDialogBuilder.show()
    }
    private fun showIntenstyDialog()
    {
        val builder : AlertDialog.Builder= AlertDialog.Builder(getActivity(),R.style.DarkAlertDialog)
        val seekBar: SeekBar = SeekBar(getActivity())
        seekBar.max=4
        seekBar.setOnSeekBarChangeListener(this);
        builder.setTitle("How is Intense the feeling?")
        builder.setView(seekBar)
        builder.setPositiveButton("save",{_,which->saveMood()})
        builder.show()

    }

    private fun saveMood()
    {

        val application = requireNotNull(this.activity).application
        val mood= Mood(
            title = moods[mCurrentMood],
            description = "the intense of mood is ${mCurrentMoodIntensity}",
            date = mCurrentMood+1,
            value = mCurrentMoodIntensity)
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



    companion object {
        fun newInstance(): HomeFragment = HomeFragment()

    }
}


