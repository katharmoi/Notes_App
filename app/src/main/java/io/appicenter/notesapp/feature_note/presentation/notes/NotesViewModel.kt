package io.appicenter.notesapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appicenter.notesapp.feature_note.domain.interactor.AddNote
import io.appicenter.notesapp.feature_note.domain.interactor.DeleteNote
import io.appicenter.notesapp.feature_note.domain.interactor.GetNotes
import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.domain.util.NoteOrder
import io.appicenter.notesapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val addNote: AddNote,
    private val getNotes: GetNotes,
    private val deleteNote: DeleteNote
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var notesJob: Job? = null

    init {
        getNotesFunc(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.Order -> {

                if (!(event.noteOrder::class == state.value.noteOrder &&
                            state.value.noteOrder.orderType == event.noteOrder.orderType)
                ) {
                    getNotesFunc(event.noteOrder)
                }
            }
            NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }

            }
            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }


    private fun getNotesFunc(noteOrder: NoteOrder) {

        notesJob?.cancel()

        notesJob = getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

}