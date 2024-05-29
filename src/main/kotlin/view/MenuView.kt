package view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun menu() {
    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            mainButtons()

            Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        }
    }
}
