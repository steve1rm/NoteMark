package me.androidbox.notes.presentation.note_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.notes.presentation.note_details.model.NoteDetailActionButtons
import me.androidbox.notes.presentation.note_details.model.NoteDetailsMode
import org.jetbrains.compose.resources.painterResource

@Composable
fun NoteDetailsActionButtons(
    selectedAction: NoteDetailsMode,
    onActionClick: (NoteDetailsMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NoteMarkTheme {
        Row(
            modifier = modifier
                .navigationBarsPadding()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            NoteDetailActionButtons.entries.forEach { detailActionButton ->
                Box(
                    modifier = Modifier.size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selectedAction == detailActionButton.noteDetailsMode) {
                                MaterialTheme.colorScheme.primary.copy(alpha = .1f)
                            } else Color.Transparent
                        )
                        .clickable(onClick = {
                            onActionClick(detailActionButton.noteDetailsMode)
                        }),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(detailActionButton.iconDrawable),
                        contentDescription = detailActionButton.noteDetailsMode.name,
                        tint = if (selectedAction == detailActionButton.noteDetailsMode) {
                            MaterialTheme.colorScheme.primary
                        } else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}