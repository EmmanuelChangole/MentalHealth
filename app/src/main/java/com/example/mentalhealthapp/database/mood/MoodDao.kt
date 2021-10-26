package com.example.mentalhealthapp.database.mood

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MoodDao
{
    @Insert
    fun insert(mood: Mood)

    @Update
    fun update(mood: Mood)

    @Delete
    fun delete(mood: Mood)

    @Query("delete from mood_database")
    fun deleteAllMood()

    @Query("select * from mood_database order by id desc")
    fun getAllMood(): LiveData<List<Mood>>

    @Query("select * from mood_database order by id desc")
     fun  getAllMoodList(): List<Mood>



}