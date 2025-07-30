@file:OptIn(ExperimentalMaterial3Api::class)

package com.champox.notes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun NotesTopAppBar(
    isFilterActive: Boolean,
    modifier: Modifier = Modifier,
    isSelectionMode: Boolean,
    selectedCount: Int = 0,
    onCancelSelection: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onViewModeClick: () -> Unit, // FIXED: this should not return anything
    onSearchClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (isSelectionMode) {
                Text(
                    text = "$selectedCount selected",
                    color = colorScheme.onBackground
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onSearchClick() },
                    color = colorScheme.surfaceVariant,
                    tonalElevation = 1.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon",
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Text(
                            text = "Search notes...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(start = 52.dp)
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (!isSelectionMode) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        painter = painterResource(id = com.champox.notes.R.drawable.ic_sidebar_shape),
                        contentDescription = "Sidebar Icon",
                        tint = colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
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
                Row {
                    IconButton(onClick = {
                        onViewModeClick() // ✅ FIXED: this line now properly invokes the lambda
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (isFilterActive) {
                                    com.champox.notes.R.drawable.filrter_icon
                                } else {
                                    com.champox.notes.R.drawable.firter_icon_off
                                }
                            ),
                            contentDescription = if (isFilterActive) "Filter active" else "Filter inactive",
                            tint = colorScheme.onBackground,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background
        )
    )
}
