package com.champox.notes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champox.notes.viewmodels.NoteCategory

@Composable
fun NoteCategorySelector(
    modifier: Modifier = Modifier,
    selected: NoteCategory,
    onSelect: (NoteCategory) -> Unit,
    onBeforeSelect: (() -> Unit)? = null, // optional callback before changing category
) {
    val items = listOf(
        Triple("All", Icons.Filled.AllInbox, NoteCategory.ALL),
        Triple("Pinned", Icons.Filled.PushPin, NoteCategory.PINNED),
        Triple("Archived", Icons.Filled.Archive, NoteCategory.ARCHIVED),
        Triple("Starred", Icons.Filled.Star, NoteCategory.FAVORITES),
    )

    LazyRow(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { (label, icon, category) ->
            val isSelected = selected == category
            val backgroundColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.surfaceVariant
            val contentColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = backgroundColor,
                modifier = Modifier.clickable {
                    onBeforeSelect?.invoke()  // Call this first if provided
                    onSelect(category)
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        color = contentColor
                    )
                }
            }
        }
    }
}
