package com.champox.notes.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var noteText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Note") },
        text = {
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.focusRequester(focusRequester),
                label = { Text("Write your note") },
                singleLine = false,
                minLines = 3
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (noteText.isNotBlank()) {
                        onConfirm(noteText)
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}