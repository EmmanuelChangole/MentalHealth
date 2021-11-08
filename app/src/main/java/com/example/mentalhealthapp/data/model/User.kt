package com.example.mentalhealthapp.data.model

import com.google.firebase.database.IgnoreExtraProperties


data class User
    (
    var username: String? = "",
    var access:String?="",
    var gender: String? = "",
    var specialist:String?="",
    var imageUrl:String?=""
)




