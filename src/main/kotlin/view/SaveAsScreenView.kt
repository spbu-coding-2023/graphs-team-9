package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.Write
import io.WriteResultOfAnalysis
import neo4jRepository
import viewModel.GraphVM
import viewModel.UndirectedGraphVM

@Composable
fun saveAsScreen(
    onClick: () -> Unit,
    graphVM: GraphVM,
) {
    var isCSVButtonEnabled by remember { mutableStateOf(true) }
    var isGraphButtonClicked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(15.dp),
    ) {
        var name by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter name") },
            value = name,
            onValueChange = { text ->
                name = text
                isCSVButtonEnabled = "/" !in name
            },
        )

        Row {
            RadioButton(selected = isGraphButtonClicked, onClick = { isGraphButtonClicked = true })
            Text("graph", modifier = Modifier.padding(vertical = 10.dp))
        }

        Row {
            RadioButton(selected = !isGraphButtonClicked, onClick = { isGraphButtonClicked = false })
            Text("analysis", modifier = Modifier.padding(vertical = 10.dp))
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (isGraphButtonClicked) {
                    Write(graphVM.graph, name)
                } else {
                    WriteResultOfAnalysis(graphVM, name)
                }
            },
            colors = ButtonDefaults.buttonColors(Color.White),
            enabled = isCSVButtonEnabled,
        ) { Text("CSV") }
        Button(modifier = Modifier.fillMaxWidth(), enabled = false,  onClick = {}, colors = ButtonDefaults.buttonColors(Color.White)) { Text("SQLite") }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = neo4jRepository != null,
            onClick = {
                if (!isGraphButtonClicked) {
                    neo4jRepository?.apply {
                        if (graphVM::class == UndirectedGraphVM::class) {
                            putAnalyzedGraphVM(graphVM, name, false)
                        } else {
                            putAnalyzedGraphVM(graphVM, name, true)
                        }
                    }
                }
                else {
                    neo4jRepository?.apply {
                        if (graphVM::class == UndirectedGraphVM::class) {
                            putGraph(graphVM.graph, name, false)
                        } else {
                            putGraph(graphVM.graph, name, true)
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color.White),
        ) { Text("Neo4j") }
        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = onClick) { Text("OK") }
    }
}
