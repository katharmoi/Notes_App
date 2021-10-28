package io.appicenter.notesapp.feature_note.presentation.notes.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.appicenter.notesapp.R
import io.appicenter.notesapp.feature_note.domain.util.NoteOrder
import io.appicenter.notesapp.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = stringResource(R.string.title),
                selected = noteOrder is NoteOrder.Title,
                onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(id = R.string.date),
                selected = noteOrder is NoteOrder.Date,
                onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(id = R.string.color),
                selected = noteOrder is NoteOrder.Color,
                onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = stringResource(id = R.string.asc),
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(noteOrder.copy(orderType = OrderType.Ascending)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(
                text = stringResource(id = R.string.desc),
                selected = noteOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(noteOrder.copy(orderType = OrderType.Descending)) }
            )

        }

    }
}


@Preview
@Composable
fun PreviewOrderSection() {
    OrderSection(onOrderChange = {})
}