package com.example.mentalhealthapp.receiver

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mentalhealthapp.R
import com.example.mentalhealthapp.data.model.User
import com.example.mentalhealthapp.database.mood.Mood

class UserViewHolder(inflater: View):
    RecyclerView.ViewHolder(inflater)
{
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView: ImageView?=null


    init {
        mTitleView = itemView.findViewById(R.id.tvName)
        mYearView = itemView.findViewById(R.id.specialist)
        mImageView=itemView.findViewById(R.id.img_holder)
    }

    fun bind(user: User) {
        mTitleView?.text = user.username
        mYearView?.text = user.specialist

    }


}