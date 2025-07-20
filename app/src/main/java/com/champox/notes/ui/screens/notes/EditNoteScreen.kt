package com.champox.notes.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.champox.notes.data.model.Note

@Composable
fun EditNoteScreen(
    note: Note,
    onSaveAndBack: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    var title by remember(note.id) { mutableStateOf(note.title) }
    var content by remember(note.id) { mutableStateOf(note.content) }
    var isEditable by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onSaveAndBack(title.trim(), content.trim())
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorScheme.primary
                )
            }

            Row {
                if (isEditable) {
                    IconButton(onClick = {
                        // Just save and switch to View mode
                        isEditable = false
                        // Optionally: Call a save function here (persist logic)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = colorScheme.primary
                        )
                    }
                } else {
                    IconButton(onClick = { isEditable = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        // TODO: Add more options (e.g. share, delete)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Scrollable + Fill Remaining Space
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Title TextField
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = colorScheme.onSurfaceVariant) },
                enabled = isEditable,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(color = colorScheme.onSurface),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                    focusedIndicatorColor = colorScheme.primary.copy(alpha = 0.87f),
                    unfocusedIndicatorColor = colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledIndicatorColor = colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledTextColor = colorScheme.onSurface,
                    cursorColor = colorScheme.primary
                )
            )

            Spacer(Modifier.height(12.dp))

            // Content TextField
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note", color = colorScheme.onSurfaceVariant) },
                enabled = isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(onDoubleTap = { isEditable = true })
                    },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.onSurface),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                    focusedIndicatorColor = colorScheme.primary.copy(alpha = 0.87f),
                    unfocusedIndicatorColor = colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledIndicatorColor = colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledTextColor = colorScheme.onSurface,
                    cursorColor = colorScheme.primary
                )
            )
        }
    }
}
