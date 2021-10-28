package io.appicenter.notesapp.feature_note.presentation.edit_note

import androidx.compose.ui.focus.FocusState

sealed class EditNoteEvent {
    data class EnterTitle(val title: String) : EditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditNoteEvent()
    data class EnterContent(val content: String) : EditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : EditNoteEvent()
    data class ChangeColor(val color: Int) : EditNoteEvent()
    object SaveNote : EditNoteEvent()
}
