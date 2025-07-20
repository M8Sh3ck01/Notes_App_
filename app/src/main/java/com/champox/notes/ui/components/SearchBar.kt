// ui/components/SearchBar.kt
package com.champox.notes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champox.notes.ui.theme.GrayLight
import com.champox.notes.ui.theme.GrayMedium
import com.champox.notes.ui.theme.PureBlack

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GrayLight, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = GrayMedium,
            modifier = Modifier.padding(end = 8.dp)
        )

        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                onSearch(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp),
            textStyle = TextStyle(
                color = PureBlack,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(PureBlack),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        "Search notes",
                        color = GrayMedium,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}