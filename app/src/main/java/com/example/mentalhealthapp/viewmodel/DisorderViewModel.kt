package com.example.mentalhealthapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mentalhealthapp.R
import com.example.mentalhealthapp.dao.DisorderDao
import com.example.mentalhealthapp.database.Disorder
import com.example.mentalhealthapp.db.DisorderDatabase
import com.example.mentalhealthapp.utils.subscribeOnBackground
import kotlinx.coroutines.launch

class DisorderViewModel(
    val database:DisorderDao,
    application:Application):AndroidViewModel(application)
{

    private val disorders=database.getAllDisorder()
    private val db = DisorderDatabase.getInstance(application)
   // internal val allDisorder : LiveData<List<Disorder>> = db.disorderDao().getAllDisorder()


    fun populateData()
    {

        subscribeOnBackground{
            database.insert(   Disorder(title ="Anxiety",description="How to deal with Anxiety",img_src = R.drawable.ic_anxiety))
            database.insert(   Disorder(title ="Anxiety",description="How to deal with Anxiety",img_src = R.drawable.ic_anxiety))
            database.insert(Disorder(title ="Apathy", description ="How to deal with Apathy", img_src =R.drawable.ic_apathy))
            database.insert( Disorder(title ="Envy", description ="How to deal with Envy", img_src =R.drawable.ic_envy))
            database.insert(  Disorder(title ="Grief", description ="How to deal with Grief",img_src = R.drawable.ic_grief))
            database.insert(  Disorder(title ="Jealously",description ="How to deal with Jealously", img_src =R.drawable.ic_jealousy))
            database.insert(  Disorder(title ="Panic",description ="How to deal with Panic", img_src =R.drawable.ic_panic))
            database.insert(   Disorder(title ="Shame",description ="How to deal with Shame", img_src =R.drawable.ic_shame))
            database.insert(  Disorder(title ="Shyness",description ="How to deal with Shyness", img_src =R.drawable.ic_shyness))
            database.insert(  Disorder(title ="Stress",description ="How to deal with Stress", img_src =R.drawable.ic_stress))
        }







    }


    fun getAllDisorder():LiveData<List<Disorder>>
    {
        return disorders
    }

    fun deleteAll() {
        subscribeOnBackground {
            database.deleteAllDisorder()
        }

    }


}