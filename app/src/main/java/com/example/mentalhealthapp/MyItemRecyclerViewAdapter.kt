package com.example.mentalhealthapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mentalhealthapp.database.Disorder

class MyItemRecyclerViewAdapter(
   // private val list: List<Disorder>
) : ListAdapter<Disorder, DisorderViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisorderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DisorderViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DisorderViewHolder, position: Int) {
        val currentItem=getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener{
                view : View ->
            view.findNavController().navigate(R.id.action_itemFragment_to_detailsFragment)
        }
    }


}


class DisorderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView:ImageView?=null


    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
        mImageView=itemView.findViewById(R.id.img_holder)
    }

    fun bind(disorder: Disorder) {
        mTitleView?.text = disorder.title
        mYearView?.text = disorder.description
        mImageView?.setImageResource(disorder.img_src)
    }}
class DiffCallback:DiffUtil.ItemCallback<Disorder>(){
    override fun areItemsTheSame(oldItem: Disorder, newItem: Disorder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Disorder, newItem: Disorder): Boolean {
       return oldItem == newItem
    }
}