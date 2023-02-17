package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.database.dao.NoteDao
import com.example.notesapp.models.model.Note
import com.example.notesapp.utilites.DATABASE_NAME

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun getNoteDao() : NoteDao

    companion object{
        @Volatile
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context : Context) : NoteDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}