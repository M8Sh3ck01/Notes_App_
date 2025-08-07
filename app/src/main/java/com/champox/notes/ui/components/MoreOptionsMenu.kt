package com.champox.notes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MoreOptionsMenuButton(
    isPinned: Boolean,
    isArchived: Boolean,
    isFavorite: Boolean,
    onPin: () -> Unit,
    onArchive: () -> Unit,
    onStar: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val iconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    val textColor = MaterialTheme.colorScheme.onSurface

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.PushPin, contentDescription = "Pin", tint = iconColor)
            },
            text = {
                Text(
                    if (isPinned) "Unpin" else "Pin",
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            onClick = {
                onPin()
                expanded = false
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.Archive, contentDescription = "Archive", tint = iconColor)
            },
            text = {
                Text(
                    if (isArchived) "Unarchive" else "Archive",
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            onClick = {
                onArchive()
                expanded = false
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    if (isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = "Star",
                    tint = iconColor
                )
            },
            text = {
                Text(
                    if (isFavorite) "Unstar" else "Star",
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            onClick = {
                onStar()
                expanded = false
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = iconColor)
            },
            text = {
                Text("Delete", color = textColor, style = MaterialTheme.typography.bodySmall)
            },
            onClick = {
                onDelete()
                expanded = false
            }
        )
    }
}