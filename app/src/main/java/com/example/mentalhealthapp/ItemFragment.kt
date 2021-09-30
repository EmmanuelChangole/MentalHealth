package com.example.mentalhealthapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mentalhealthapp.databinding.FragmentItemListBinding
import com.example.mentalhealthapp.db.DisorderDatabase
import com.example.mentalhealthapp.utils.subscribeOnBackground
import com.example.mentalhealthapp.viewmodel.DisorderViewModel
import com.example.mentalhealthapp.viewmodel.DisorderViewModelFactory
import kotlinx.android.synthetic.main.fragment_item_list.*

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    private val  disorder=null
    private lateinit var disorderViewModel: DisorderViewModel
    private lateinit var adapter :MyItemRecyclerViewAdapter
    val TAG= ItemFragment::class.java.simpleName as String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


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
        val binding: FragmentItemListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_item_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = DisorderDatabase.getInstance(application).disorderDatabaseDao
        val viewModelFactory = DisorderViewModelFactory(dataSource, application)
         disorderViewModel = ViewModelProvider(
            this, viewModelFactory).get(DisorderViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.disorderViewModel=disorderViewModel
        disorderViewModel.deleteAll()
        disorderViewModel.populateData()

        return   binding.root;




    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        val myItemRecyclerViewAdapter=MyItemRecyclerViewAdapter();
        list_recycler_view.apply{
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter=myItemRecyclerViewAdapter
        }
        disorderViewModel.getAllDisorder().observe(viewLifecycleOwner, Observer {
          myItemRecyclerViewAdapter.submitList(it)

        })

    }

    companion object {
        fun newInstance(): ItemFragment = ItemFragment()

    }
}