package com.example.mentalhealthapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mentalhealthapp.database.Disorder



@Dao
interface DisorderDao
{
    @Insert
     fun insert(disorder: Disorder)

    @Update
     fun update(disorder: Disorder)

    @Delete
      fun delete(disorder: Disorder)

    @Query("delete from disorder_database")
    fun deleteAllDisorder()

    @Query("select * from disorder_database order by id desc")
     fun getAllDisorder(): LiveData<List<Disorder>>

}