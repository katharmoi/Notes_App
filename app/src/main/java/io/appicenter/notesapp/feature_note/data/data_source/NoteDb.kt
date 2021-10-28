package io.appicenter.notesapp.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import io.appicenter.notesapp.feature_note.domain.model.Note

@Database(
    version = 1,
    entities = [Note::class]
)
abstract class NoteDb : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}