package io.appicenter.notesapp.feature_note.presentation.edit_note.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ColorsSelector(
    modifier: Modifier = Modifier,
    colorsList: List<Color> = emptyList(),
    size: Dp = 50.dp,
    shadowSize: Dp = 15.dp,
    shape: Shape = CircleShape,
    borderWith: Dp = 3.dp,
    isSelected: Boolean = false,
    selectedBorderColor: Color = Color.Black,
    unselectedBorderColor: Color = Color.Transparent,
    scope: CoroutineScope,
    animatable: Animatable<Color, AnimationVector4D>,
    animationDuration: Int = 500,
    onSelected: (color: Int) -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        colorsList.forEach { color ->
            val colorInt = color.toArgb()

            Box(
                modifier = Modifier
                    .size(size)
                    .shadow(shadowSize, shape)
                    .clip(shape)
                    .background(color)
                    .border(
                        width = borderWith,
                        color = if (isSelected) selectedBorderColor else unselectedBorderColor,
                        shape = shape
                    )
                    .clickable {
                        scope.launch {
                            animatable.animateTo(
                                targetValue = Color(colorInt),
                                animationSpec = tween(
                                    durationMillis = animationDuration
                                )
                            )
                        }

                        onSelected(colorInt)
                    }
            )
        }

    }

}
