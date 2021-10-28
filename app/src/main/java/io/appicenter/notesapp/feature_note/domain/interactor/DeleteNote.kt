package io.appicenter.notesapp.feature_note.domain.interactor

import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}