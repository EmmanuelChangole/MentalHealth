package com.example.mentalhealthapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mentalhealthapp.dao.DisorderDao
import com.example.mentalhealthapp.database.Disorder


@Database(entities = [Disorder::class], version = 1,exportSchema = false)
abstract class DisorderDatabase:RoomDatabase()
{
    abstract val disorderDatabaseDao: DisorderDao
   // abstract fun disorderDao(): DisorderDao
    companion object {
        @Volatile
        private var INSTANCE: DisorderDatabase? = null

        @Synchronized
        fun getInstance(context: Context): DisorderDatabase {
           synchronized(this)
           {
               var instance= INSTANCE
               if(instance == null)
               {
                 instance= Room.databaseBuilder(
                       context.applicationContext,
                       DisorderDatabase::class.java,
                       "disorder_database"
                   ).fallbackToDestructiveMigration().build()

                INSTANCE=instance


               }

               return instance;
           }
        }



       /* private fun populateDatabase(db: DisorderDatabase) {
            val disorderDao = db.disorderDao()
            subscribeOnBackground {
                disorderDao.insert(   Disorder(title ="Anxiety",description="How to deal with Anxiety",img_src = R.drawable.ic_anxiety))
                disorderDao.insert(   Disorder(title ="Anxiety",description="How to deal with Anxiety",img_src = R.drawable.ic_anxiety))
                disorderDao.insert(Disorder(title ="Apathy", description ="How to deal with Apathy", img_src =R.drawable.ic_apathy))
                disorderDao.insert( Disorder(title ="Envy", description ="How to deal with Envy", img_src =R.drawable.ic_envy))
                disorderDao.insert(  Disorder(title ="Grief", description ="How to deal with Grief",img_src = R.drawable.ic_grief))
                disorderDao.insert(  Disorder(title ="Jealously",description ="How to deal with Jealously", img_src =R.drawable.ic_jealousy))
                disorderDao.insert(  Disorder(title ="Panic",description ="How to deal with Panic", img_src =R.drawable.ic_panic))
                disorderDao.insert(   Disorder(title ="Shame",description ="How to deal with Shame", img_src =R.drawable.ic_shame))
                disorderDao.insert(  Disorder(title ="Shyness",description ="How to deal with Shyness", img_src =R.drawable.ic_shyness))
                disorderDao.insert(  Disorder(title ="Stress",description ="How to deal with Stress", img_src =R.drawable.ic_stress))
            }



        }*/

    }

}