package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun sqliteScreen() {
    Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))

    Column(modifier = Modifier.padding(20.dp)) {
        var uri by remember { mutableStateOf("") }
        OutlinedTextField(
            placeholder = { Text("Enter URI") },
            modifier =
                Modifier
                    .fillMaxWidth(),
            value = uri,
            onValueChange = { text ->
                uri = text
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        var database by remember { mutableStateOf("") }
        OutlinedTextField(
            placeholder = { Text("Enter database name") },
            modifier =
                Modifier
                    .fillMaxWidth(),
            value = database,
            onValueChange = { text ->
                database = text
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        var username by remember { mutableStateOf("") }
        OutlinedTextField(
            placeholder = { Text("Enter user name") },
            modifier =
                Modifier
                    .fillMaxWidth(),
            value = username,
            onValueChange = { text ->
                username = text
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        var password by remember { mutableStateOf("") }
        OutlinedTextField(
            placeholder = { Text("Enter password") },
            modifier =
                Modifier
                    .fillMaxWidth(),
            value = password,
            onValueChange = { text ->
                password = text
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            colors = ButtonDefaults.buttonColors(Color.White),
            enabled = false,
            modifier =
                Modifier
                    .width(100.dp)
                    .height(45.dp)
                    .align(Alignment.End),
            onClick = {},
        ) {
            Text("Apply")
        }
    }
}
