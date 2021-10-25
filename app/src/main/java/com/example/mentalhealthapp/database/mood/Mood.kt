package com.example.mentalhealthapp.database.mood

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_database")
data class Mood
    (
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "intense")
    val description: String,
)
