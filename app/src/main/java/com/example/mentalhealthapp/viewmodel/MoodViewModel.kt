package com.example.mentalhealthapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mentalhealthapp.database.disorder.Disorder
import com.example.mentalhealthapp.database.disorder.DisorderDao
import com.example.mentalhealthapp.database.disorder.DisorderDatabase
import com.example.mentalhealthapp.database.mood.Mood
import com.example.mentalhealthapp.database.mood.MoodDao
import com.example.mentalhealthapp.database.mood.MoodDatabase
import com.example.mentalhealthapp.utils.subscribeOnBackground

class MoodViewModel
    (
    val database: MoodDao,
    application: Application
):AndroidViewModel(Application())
{
    private val mood=database.getAllMood()
    private val db = MoodDatabase.getInstance(application)

    public fun addMood(mood: Mood)
    { subscribeOnBackground { database.insert(mood) } }

    public fun deleteMood(mood: Mood)
    { subscribeOnBackground { database.delete(mood) }}

    public fun deleteAll()
    { subscribeOnBackground { database.deleteAllMood() }}

    fun getAllMoods(): LiveData<List<Mood>>
    {
        return mood
    }


}