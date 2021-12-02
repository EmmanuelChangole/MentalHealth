package com.example.mentalhealthapp.receiver

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mentalhealthapp.R
import com.example.mentalhealthapp.data.model.User
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class UserViewHolder(inflater: View):
    RecyclerView.ViewHolder(inflater)
{
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mImageView: CircleImageView?=null


    init {
        mTitleView = itemView.findViewById(R.id.tvName)
        mYearView = itemView.findViewById(R.id.specialist)
        mImageView=itemView.findViewById(R.id.img_holder)
    }

    fun bind(user: User, context: Context?)
    {

        mTitleView?.text = user.username
        mYearView?.text = user.specialist
        mImageView?.let {
            if (context != null) {
                Glide.with(context)
                    .load(user.imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .into(it)

            }
        }


    }


}