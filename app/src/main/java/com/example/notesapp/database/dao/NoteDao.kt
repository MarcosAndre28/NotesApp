package com.example.notesapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesapp.models.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE notes Set title = :title, note = :note WHERE id = :id")
    suspend fun update(id : Int?, title : String?, note : String?)

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes() : LiveData<List<Note>>

}