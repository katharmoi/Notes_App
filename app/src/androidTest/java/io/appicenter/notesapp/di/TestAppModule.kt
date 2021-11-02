package io.appicenter.notesapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appicenter.notesapp.feature_note.data.data_source.NoteDb
import io.appicenter.notesapp.feature_note.data.repository.NoteRepositoryImpl
import io.appicenter.notesapp.feature_note.domain.interactor.AddNote
import io.appicenter.notesapp.feature_note.domain.interactor.DeleteNote
import io.appicenter.notesapp.feature_note.domain.interactor.GetNote
import io.appicenter.notesapp.feature_note.domain.interactor.GetNotes
import io.appicenter.notesapp.feature_note.domain.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): NoteDb {
        return Room.inMemoryDatabaseBuilder(
            app,
            NoteDb::class.java
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(db: NoteDb): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Singleton
    @Provides
    fun provideAddNoteUseCase(repo: NoteRepository): AddNote {
        return AddNote(repo)
    }

    @Singleton
    @Provides
    fun provideGetNotesUseCase(repo: NoteRepository): GetNotes {
        return GetNotes(repo)
    }

    @Singleton
    @Provides
    fun provideGetNoteUseCase(repo: NoteRepository): GetNote {
        return GetNote(repo)
    }

    @Singleton
    @Provides
    fun provideDeleteNoteUseCase(repo: NoteRepository): DeleteNote {
        return DeleteNote(repo)
    }


}