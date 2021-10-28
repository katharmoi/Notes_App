package io.appicenter.notesapp.feature_note.domain.interactor

import io.appicenter.notesapp.feature_note.domain.model.InvalidNoteException
import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title can not be empty")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("The content can not be empty")
        }

        repository.addNote(note)
    }
}
