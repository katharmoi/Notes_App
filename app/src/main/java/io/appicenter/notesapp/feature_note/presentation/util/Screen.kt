package io.appicenter.notesapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen : Screen(NavigationKeys.Route.NOTES_SCREEN)
    object EditNotesScreen : Screen(NavigationKeys.Route.EDIT_NOTES_SCREEN)
}
