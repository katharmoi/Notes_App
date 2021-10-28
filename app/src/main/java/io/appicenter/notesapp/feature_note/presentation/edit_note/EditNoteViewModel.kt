package io.appicenter.notesapp.feature_note.presentation.edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appicenter.notesapp.feature_note.domain.interactor.AddNote
import io.appicenter.notesapp.feature_note.domain.interactor.GetNote
import io.appicenter.notesapp.feature_note.domain.model.InvalidNoteException
import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.presentation.util.NavigationKeys
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val addNote: AddNote,
    private val getNote: GetNote,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitleState = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter Title..."
        )
    )
    val noteTitleState: State<NoteTextFieldState> = _noteTitleState

    private val _noteContentState = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter Content..."
        )
    )
    val noteContentState: State<NoteTextFieldState> = _noteContentState

    private val _noteColorState = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColorState: State<Int> = _noteColorState

    private val _effectsFlow = MutableSharedFlow<UIEffect>()
    val effectsFlow: SharedFlow<UIEffect> = _effectsFlow

    private var currentNodeId: Int? = null

    init {
        savedStateHandle.get<Int>(NavigationKeys.Args.NOTE_ID)?.let { noteId ->

            if (noteId != -1) {
                viewModelScope.launch {
                    getNote(noteId)?.also { note ->
                        currentNodeId = note.id
                        _noteTitleState.value = noteTitleState.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteContentState.value = noteContentState.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )

                        _noteColorState.value = note.color
                    }

                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {

        when (event) {
            is EditNoteEvent.ChangeColor -> {
                _noteColorState.value = event.color
            }
            is EditNoteEvent.ChangeContentFocus -> {
                _noteContentState.value = noteContentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContentState.value.text.isBlank()
                )

            }
            is EditNoteEvent.ChangeTitleFocus -> {
                _noteTitleState.value = noteTitleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitleState.value.text.isBlank()
                )
            }
            is EditNoteEvent.EnterContent -> {
                _noteContentState.value = noteContentState.value.copy(
                    text = event.content
                )
            }
            is EditNoteEvent.EnterTitle -> {
                _noteTitleState.value = noteTitleState.value.copy(
                    text = event.title
                )
            }
            EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        addNote(
                            Note(
                                title = noteTitleState.value.text,
                                content = noteContentState.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColorState.value,
                                id = currentNodeId

                            )
                        )

                        _effectsFlow.emit(UIEffect.SaveNote)

                    } catch (e: InvalidNoteException) {
                        _effectsFlow.emit(
                            UIEffect.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }

                }
            }
        }
    }

    sealed class UIEffect {
        data class ShowSnackbar(val message: String) : UIEffect()
        object SaveNote : UIEffect()
    }

}