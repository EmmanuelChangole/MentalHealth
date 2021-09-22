package com.example.mentalhealthapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.mentalhealthapp.data.model.Disorder

class MyItemRecyclerViewAdapter(
    private val list: List<Disorder>
) : RecyclerView.Adapter<DisorderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisorderViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        return DisorderViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DisorderViewHolder, position: Int) {
        val disorder: Disorder = list[position]
        holder.bind(disorder)
        holder.itemView.setOnClickListener{
                view : View ->
            view.findNavController().navigate(R.id.action_itemFragment_to_detailsFragment)
        }
    }

    override fun getItemCount(): Int =list.size


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