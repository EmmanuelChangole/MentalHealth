package com.example.mentalhealthapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mentalhealthapp.database.mood.MoodDao
import java.lang.IllegalArgumentException

class MoodViewModelFactory
    (
    private val datasource: MoodDao,
    private val application: Application
    ):ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(MoodViewModel::class.java))
        {
            return MoodViewModel(datasource,application) as T;
        }

        throw IllegalArgumentException("Unknown viewModel class")

    }

}