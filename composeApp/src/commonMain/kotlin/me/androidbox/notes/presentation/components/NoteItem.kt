package me.androidbox.notes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import me.androidbox.core.presentation.utils.toFormattedDate
import me.androidbox.isAtLeastMedium
import me.androidbox.isTablet
import me.androidbox.notes.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    val isTablet = isTablet()
    val textLengthLimitation = if (isTablet) 250 else 150
    val contentText = remember(isTablet, textLengthLimitation) {
        if (note.content.length > textLengthLimitation) {
            note.content.take(textLengthLimitation).plus("...")
        } else note.content.take(textLengthLimitation)
    }
    val titleStyle = if (isTablet) MaterialTheme.typography.titleLarge else
        MaterialTheme.typography.titleMedium
    val contentStyle = if (isTablet) MaterialTheme.typography.bodyLarge else
        MaterialTheme.typography.bodySmall
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = true,
                ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .08f),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .08f)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp)
    ) {
        Text(
            text = note.createdAt.toFormattedDate(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = titleStyle
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contentText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = contentStyle
        )
    }
}