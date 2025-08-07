
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteConfirmationDialog(
    visible: Boolean,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    itemCount: Int = 1
) {
    if (!visible) return

    val message = if (itemCount > 1) {
        "Are you sure you want to delete these $itemCount notes?"
    } else {
        "Are you sure you want to delete this note?"
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Note${if (itemCount > 1) "s" else ""}") },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red, // 🔴 Red background
                    contentColor = Color.White  // ⬜ White text
                )
            ) {
                Text("Delete")
            }
        }
        ,
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
