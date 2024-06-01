package view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun alertDialogView(
    onClick: () -> Unit,
    title: String,
    text: String,
) {
    AlertDialog(
        title = { Text(title, fontSize = 20.sp) },
        text = { Text(text, fontSize = 15.sp) },
        onDismissRequest = onClick,
        confirmButton = {
            Button(
                onClick = onClick,
            ) {
                Text("OK", fontSize = 15.sp)
            }
        },
    )
}
