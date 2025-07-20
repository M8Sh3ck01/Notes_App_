package com.champox.notes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    isSelectionMode: Boolean,
    selectedCount: Int = 0,
    onCancelSelection: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onViewModeClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (isSelectionMode) "$selectedCount selected" else "Notes",
                color = colorScheme.onBackground
            )
        },
        navigationIcon = {
            if (!isSelectionMode) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            if (isSelectionMode) {
                Text(
                    text = "Cancel",
                    color = colorScheme.onBackground,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onCancelSelection() }
                )
            } else {
                IconButton(onClick = onViewModeClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "View Mode",
                        tint = colorScheme.onBackground
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background
        )
    )
}
