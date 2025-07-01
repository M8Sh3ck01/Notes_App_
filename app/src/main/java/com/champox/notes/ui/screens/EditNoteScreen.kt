package com.champox.notes.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.champox.notes.data.Note

@Composable
fun EditNoteScreen(
    note: Note,
    onSaveAndBack: (String, String) -> Unit, // ← Renamed to reflect back+save
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    val counter: Number by remember { mutableIntStateOf(1) }



    var isEditable by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Top Row with Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {

                val safeTitle = if (title.isBlank()){ "Untitled $counter"} else title.trim()
                onSaveAndBack(safeTitle, content.trim())
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }

            if (isEditable) {
                Button(onClick = {
                    val safeTitle = if (title.isBlank()) "Untitled Note" else title.trim()
                    onSaveAndBack(safeTitle, content.trim())
                }) {
                    Text("Done")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Title Field
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            enabled = isEditable,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        // Content Field with double-tap to unlock
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("(double tap to edit)") },
            enabled = isEditable,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            isEditable = true // 🔓 Enable editing
                        }
                    )
                },
            singleLine = false,
            maxLines = Int.MAX_VALUE,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}
