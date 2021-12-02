package com.example.mentalhealthapp

import android.app.Application
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mentalhealthapp.database.mood.Mood

class MyItemRecyclerViewAdapter(
   // private val list: List<Disorder>
) : ListAdapter<Mood, DisorderViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisorderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DisorderViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DisorderViewHolder, position: Int) {
        val currentItem=getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener(){
            view : View ->
            onClick(currentItem.title ,view);
        }
    }

    fun get_Item(position: Int):Mood
    {
        var currentItem=getItem(position)
        return currentItem;

    }


    private fun onClick(title:String,view: View)
    {
       val bundle= bundleOf("title" to  title)
       view.findNavController().navigate(R.id.action_itemFragment2_to_detailsFragment2,bundle)

    }


}


class DisorderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView: ImageView?=null


    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
       mImageView=itemView.findViewById(R.id.img_holder)
    }

    fun bind(mood: Mood) {
        mTitleView?.text = mood.title
        mYearView?.text = mood.description
        if(mood.title =="Sad")
        {
            mImageView?.setImageResource(R.drawable.ic_sad)
        }
        else if(mood.title =="Disgusted")
        {
            mImageView?.setImageResource(R.drawable.ic_disgusted)
        }
        else if(mood.title =="Fearful")
        {
            mImageView?.setImageResource(R.drawable.ic_panic)
        }

        else if(mood.title =="Bad")
        {
            mImageView?.setImageResource(R.drawable.ic_stress)
        }
        else if(mood.title =="Angry")
        {
            mImageView?.setImageResource(R.drawable.ic_angry)
        }
        else if(mood.title =="Scared")
        {

        }
        else if(mood.title =="Surprised")
        {
            mImageView?.setImageResource(R.drawable.ic_suprised)
        }
        else
        {
            mImageView?.setImageResource(R.drawable.ic_happy)

        }





    }}


class DiffCallback:DiffUtil.ItemCallback<Mood>(){
    override fun areItemsTheSame(oldItem: Mood, newItem: Mood): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Mood, newItem: Mood): Boolean {
       return oldItem == newItem
    }
}