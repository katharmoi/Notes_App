package io.appicenter.notesapp.feature_note.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.appicenter.notesapp.feature_note.data.FakeNoteRepository
import io.appicenter.notesapp.feature_note.domain.model.InvalidNoteException
import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteTest {

    private lateinit var addNote: AddNote

    private lateinit var repo: NoteRepository


    @Before
    fun setUp() {
        repo = FakeNoteRepository()
        addNote = AddNote(repo)

    }

    @Test(expected = InvalidNoteException::class)
    fun `When note title is empty, should throw InvalidNoteException`() = runBlocking {
        addNote(Note(title = "", content = "Test content", timestamp = 0L, color = 0))
    }

    @Test(expected = InvalidNoteException::class)
    fun `When note content is empty, should throw InvalidNoteException`() = runBlocking {
        addNote(Note(title = "Test title", content = "", timestamp = 0L, color = 0))
    }

    @Test
    fun `When note title and content is not empty, should add note`() = runBlocking {
        addNote(Note(title = "Test title", content = "Test content", timestamp = 0L, color = 0))

        val note = repo.getNotes().first()
        assertThat(note[0].title).isEqualTo("Test title")
    }


}