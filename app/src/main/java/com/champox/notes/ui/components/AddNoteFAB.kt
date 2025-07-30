// ui/components/AddNoteFAB.kt
package com.champox.notes.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddNoteFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.padding(16.dp),
        containerColor = Color.White,    // use primary from theme
        contentColor = Color.Black     // use onPrimary from theme
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            modifier = Modifier.size(24.dp)
        )
    }
}
