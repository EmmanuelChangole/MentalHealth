package com.example.mentalhealthapp.database.disorder

import androidx.lifecycle.LiveData
import androidx.room.*


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