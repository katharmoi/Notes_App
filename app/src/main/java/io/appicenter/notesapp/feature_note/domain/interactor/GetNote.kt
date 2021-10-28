package io.appicenter.notesapp.feature_note.domain.interactor

import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {

        return repository.getNoteById(id)
    }
}