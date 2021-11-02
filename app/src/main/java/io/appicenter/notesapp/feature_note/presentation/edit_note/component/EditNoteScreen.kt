package io.appicenter.notesapp.feature_note.presentation.edit_note.component

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.appicenter.notesapp.R
import io.appicenter.notesapp.core.util.TestTags
import io.appicenter.notesapp.feature_note.domain.model.Note
import io.appicenter.notesapp.feature_note.presentation.edit_note.EditNoteEvent
import io.appicenter.notesapp.feature_note.presentation.edit_note.EditNoteViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNoteScreen(
    navController: NavController,
    viewModel: EditNoteViewModel = hiltViewModel(),
    emptyNoteColor: Int
) {

    val titleState = viewModel.noteTitleState.value
    val contentState = viewModel.noteContentState.value
    val scaffoldState = rememberScaffoldState()


    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (emptyNoteColor != -1) emptyNoteColor else viewModel.noteColorState.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.effectsFlow.collectLatest { effect ->
            when (effect) {
                EditNoteViewModel.UIEffect.SaveNote -> {
                    navController.navigateUp()
                }
                is EditNoteViewModel.UIEffect.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = effect.message
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(EditNoteEvent.SaveNote) },
                backgroundColor = MaterialTheme.colors.primary
            ) {

                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(id = R.string.save_note)
                )

            }
        },
        scaffoldState = scaffoldState
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            ColorsSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colorsList = Note.noteColors,
                scope = scope,
                animatable = noteBackgroundAnimatable
            ) {
                viewModel.onEvent(EditNoteEvent.ChangeColor(it))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(EditNoteEvent.EnterTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(EditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                isSingleLine = true,
                testTag = TestTags.TITLE_TEXT_FIELD,
                textStyle = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(EditNoteEvent.EnterContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(EditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                testTag = TestTags.CONTENT_TEXT_FIELD,
                modifier = Modifier.fillMaxHeight()
            )

        }

    }

}