package com.example.mentalhealthapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mentalhealthapp.data.model.Disorder
import kotlinx.android.synthetic.main.fragment_item_list.*

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    private val  disorder = listOf(
        Disorder("Anxiety","How to deal with Anxiety",R.drawable.ic_anxiety),
        Disorder("Apathy","How to deal with Apathy",R.drawable.ic_apathy),
        Disorder("Envy","How to deal with Envy",R.drawable.ic_envy),
        Disorder("Grief","How to deal with Grief",R.drawable.ic_grief),
        Disorder("Jealously","How to deal with Jealously",R.drawable.ic_jealousy),
        Disorder("Panic","How to deal with Panic",R.drawable.ic_panic),
        Disorder("Shame","How to deal with Shame",R.drawable.ic_shame),
        Disorder("Shyness","How to deal with Shyness",R.drawable.ic_shyness),
        Disorder("Stress","How to deal with Stress",R.drawable.ic_stress),

    )


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
      return   inflater.inflate(R.layout.fragment_item_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        list_recycler_view.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = MyItemRecyclerViewAdapter(disorder)
        }


    }

    companion object {
        fun newInstance(): ItemFragment = ItemFragment()

    }
}