package com.example.mentalhealthapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mentalhealthapp.database.disorder.DisorderDao
import java.lang.IllegalArgumentException

class DisorderViewModelFactory(
    private val datasource: DisorderDao,
    private val application:Application
            ):ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DisorderViewModel::class.java))
        {
            return DisorderViewModel(datasource,application) as T

        }
        throw IllegalArgumentException("Unknown viewModel class")

    }

}