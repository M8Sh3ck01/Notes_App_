package com.champox.notes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.champox.notes.data.local.session.UserSession
import com.champox.notes.viewmodels.NoteCategory


@Composable
fun NotesAppDrawer(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingsClick: ()-> Unit
) {

    Column(
        modifier = modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 1. App Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 1.dp)
        ) {
            Text(
                text = "Notes"
                , style = typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary   ,
                modifier = Modifier.padding(start = 12.dp)


            )
        }


        Column(modifier = Modifier.padding(bottom = 15.dp)) {

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Account",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = UserSession.currentUserEmail ?: "No Email",
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 2. Main Filters
        DrawerSectionTitle("Filters")
        DrawerItem(
            icon = Icons.AutoMirrored.Filled.Note,
            label = "All Notes",
            isSelected = selectedCategory == NoteCategory.ALL.name,
            onClick = { onCategorySelected(NoteCategory.ALL.name) }
        )
        DrawerItem(
            icon = Icons.Default.PushPin,
            label = "Pinned",
            isSelected = selectedCategory == NoteCategory.PINNED.name,
            onClick = { onCategorySelected(NoteCategory.PINNED.name) }
        )
        DrawerItem(
            icon = Icons.Default.Star,
            label = "Starred",
            isSelected = selectedCategory == NoteCategory.FAVORITES.name,
            onClick = { onCategorySelected(NoteCategory.FAVORITES.name) }
        )
        DrawerItem(
            icon = Icons.Default.Archive,
            label = "Archived",
            isSelected = selectedCategory == NoteCategory.ARCHIVED.name,
            onClick = { onCategorySelected(NoteCategory.ARCHIVED.name) }
        )

        //Divider(modifier = Modifier.padding(vertical = 16.dp))



        //Spacer(modifier = Modifier.weight(1f))

        // 4. App Actions
        Divider(modifier = Modifier.padding(bottom = 16.dp))
        DrawerItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            onClick = { onSettingsClick() } // <-- pass this as a lambda from your main screen

        )


        DrawerItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = "Sign Out",
            tint = MaterialTheme.colorScheme.error,
            onClick = onSignOut
        )


    }
}

// Reusable Drawer Item Component
@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else Color.Transparent

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else tint

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = typography.bodyMedium,
            color = contentColor
        )
    }
}

// Section Header
@Composable
private fun DrawerSectionTitle(text: String) {
    Text(
        text = text,
        style = typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
    )
}