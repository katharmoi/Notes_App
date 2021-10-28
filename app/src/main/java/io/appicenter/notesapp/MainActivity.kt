package io.appicenter.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.appicenter.notesapp.feature_note.presentation.edit_note.component.EditNoteScreen
import io.appicenter.notesapp.feature_note.presentation.notes.component.NotesScreen
import io.appicenter.notesapp.feature_note.presentation.util.NavigationKeys
import io.appicenter.notesapp.feature_note.presentation.util.Screen
import io.appicenter.notesapp.ui.theme.NotesAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }

                        composable(route = Screen.EditNotesScreen.route +
                                "?${NavigationKeys.Args.NOTE_ID}={noteId}" +
                                "&${NavigationKeys.Args.NOTE_COLOR}={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },

                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )

                        ) {
                            val color = it.arguments?.getInt(NavigationKeys.Args.NOTE_COLOR) ?: -1
                            EditNoteScreen(
                                navController = navController,
                                emptyNoteColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}
