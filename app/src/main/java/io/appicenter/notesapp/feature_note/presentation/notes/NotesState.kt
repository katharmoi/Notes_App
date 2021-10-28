package io.appicenter.notesapp.feature_note.presentation.notes

import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.util.NoteOrder
import io.appicenter.notesapp.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
