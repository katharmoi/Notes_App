package io.appicenter.notesapp.feature_note.presentation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.appicenter.notesapp.MainActivity
import io.appicenter.notesapp.R
import io.appicenter.notesapp.core.util.TestTags
import io.appicenter.notesapp.di.AppModule
import io.appicenter.notesapp.feature_note.presentation.edit_note.component.EditNoteScreen
import io.appicenter.notesapp.feature_note.presentation.notes.component.NotesScreen
import io.appicenter.notesapp.feature_note.presentation.util.NavigationKeys
import io.appicenter.notesapp.feature_note.presentation.util.Screen
import io.appicenter.notesapp.ui.theme.NotesAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get :Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    lateinit var context: Context

    @Before
    @ExperimentalAnimationApi
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {

            NotesAppTheme {

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

        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun saveNewNote_ThenEdit() {

        val testTitle = "test_title"
        val testContent = "test_content"

        //Add a new note
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.add_note))
            .performClick()

        //Enter title
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput(testTitle)

        //Enter content
        composeRule
            .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput(testContent)

        //Save note
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.save_note))
            .performClick()

        //On the notes screen assert that saved note is displayed
        composeRule
            .onNodeWithText(testTitle)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(testContent)
            .assertIsDisplayed()

        //Click the note
        composeRule
            .onNodeWithText(testTitle)
            .performClick()

        //On edit screen assert that same note is displayed
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals(testTitle)

        composeRule
            .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .assertTextEquals(testContent)

    }

    @Test
    fun saveNewNotes_ThenOrderByTitleDescending() {

        val testTitle = "test_title"
        val testContent = "test_content"

        for (i in 1..3) {
            //Add a new note
            composeRule
                .onNodeWithContentDescription(context.getString(R.string.add_note))
                .performClick()

            //Enter title
            composeRule
                .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput(testTitle + '_' + i.toString())

            //Enter content
            composeRule
                .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
                .performTextInput(testContent)

            //Save note
            composeRule
                .onNodeWithContentDescription(context.getString(R.string.save_note))
                .performClick()
        }

        //Assert new nodes are displayed
        composeRule
            .onNodeWithText(testTitle + "_1")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText(testTitle + "_2")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText(testTitle + "_3")
            .assertIsDisplayed()

        //Click order by title radio
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.title))
            .performClick()

        //Click descending radio
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.desc))
            .performClick()

        composeRule
            .onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
            .assertTextContains("3")

        composeRule
            .onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
            .assertTextContains("2")

        composeRule
            .onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
            .assertTextContains("1")

    }


}
