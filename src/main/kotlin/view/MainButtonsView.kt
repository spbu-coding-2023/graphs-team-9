package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import graph.GraphInfo
import graph.Neo4jRepository
import graph.UndirectedGraph
import io.ReadResultOfAnalysis
import viewModel.*

enum class ButtonPressed {
    None,
    ChooseFile,
    PostgreSQL,
    Neo4j
}

enum class Screen {
    Menu,
    UndirectedGraph,
    DirectedGraph
}

private lateinit var directedGraphVM: DirectedGraphVM
private lateinit var undirectedGraphVM: UndirectedGraphVM

@Composable
fun mainButtons() {
    var screenState by remember { mutableStateOf(Screen.Menu) }
    var buttonState by remember { mutableStateOf(ButtonPressed.None) }

    when (screenState) {

        Screen.Menu -> {
            Column(modifier = Modifier.padding(15.dp)) {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp),
                    onClick = {
                        buttonState = MainButtonVM(buttonState).onClick(ButtonPressed.ChooseFile)
                    },
                ) {
                    if (buttonState == ButtonPressed.ChooseFile) {
                        Text("❌", textAlign = TextAlign.Center, fontSize = 30.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add file",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(Color.White),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp),
                    onClick = {
                        buttonState = MainButtonVM(buttonState).onClick(ButtonPressed.PostgreSQL)
                    },
                ) {
                    if (buttonState == ButtonPressed.PostgreSQL) {
                        Text("❌", textAlign = TextAlign.Center, fontSize = 30.sp)
                    } else {
                        Text("SQLite", textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(Color.White),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp),
                    onClick = {
                        buttonState = MainButtonVM(buttonState).onClick(ButtonPressed.Neo4j)
                    },
                ) {
                    if (buttonState == ButtonPressed.Neo4j) {
                        Text("❌", textAlign = TextAlign.Center, fontSize = 30.sp)
                    } else {
                        Text("Neo4j", textAlign = TextAlign.Center)
                    }
                }
            }

            Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))

            when (buttonState) {

                ButtonPressed.ChooseFile -> {
                    var file by remember { mutableStateOf("") }
                    var files by remember { mutableStateOf(listOf<String>()) }
                    var path by remember { mutableStateOf("graphs/") }
                    var isErrorOccurred by remember { mutableStateOf(false) }

                    if (isErrorOccurred) {
                        AlertDialog(
                            title = { Text("Error", fontSize = 20.sp) },
                            text = { Text("Can't read file", fontSize = 15.sp) },
                            onDismissRequest = { isErrorOccurred = false },
                            confirmButton = {
                                Button(
                                    onClick = { isErrorOccurred = false }
                                ) {
                                    Text("OK", fontSize = 15.sp)
                                }
                            }
                        )
                    }

                    Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))

                    files = TextFieldVM(path).addFilesToList(file)

                    Column(modifier = Modifier.padding(20.dp)) {
                        OutlinedTextField(
                            placeholder = { Text("Enter path to .csv file") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = file,
                            onValueChange = { text ->
                                file = text
                                path = TextFieldVM(path).changePath(file, files)
                                files = TextFieldVM(path).addFilesToList(file)
                            },
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            items(files) { currentFile ->
                                Text(
                                    text = currentFile,
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(5.dp)
                                        .clickable(
                                            onClick = {
                                                if (currentFile.substringAfterLast(".") == "csv") {
                                                    try {
                                                        val graph = FileListVM(currentFile).getGraph()
                                                        screenState = FileListVM(currentFile).defineGraphType(graph)
                                                        when (screenState) {
                                                            Screen.UndirectedGraph -> undirectedGraphVM = UndirectedGraphVM(graph)
                                                            else -> directedGraphVM = DirectedGraphVM(graph)
                                                        }
                                                        buttonState = ButtonPressed.None
                                                    }
                                                    catch (e: IllegalArgumentException) {
                                                        try {
                                                            val graphVM = FileListVM(currentFile).getGraphVM()
                                                            screenState = FileListVM(currentFile).defineGraphVMType(graphVM)
                                                            when (screenState) {
                                                                Screen.UndirectedGraph -> undirectedGraphVM = graphVM as UndirectedGraphVM
                                                                else -> directedGraphVM = graphVM as DirectedGraphVM
                                                            }
                                                            buttonState = ButtonPressed.None
                                                        }
                                                        catch (e: IllegalArgumentException) { isErrorOccurred = true }
                                                    }
                                                } else {
                                                    file = "$currentFile/"
                                                    path = "graphs/$file"
                                                    files = TextFieldVM(path).addFilesToList(file)
                                                }
                                            },
                                        ),
                                )
                                Divider()
                            }
                        }
                    }
                }
                ButtonPressed.PostgreSQL -> postgreSQLScreen()
                ButtonPressed.Neo4j -> {
                    var graphsList by remember { mutableStateOf(arrayListOf(GraphInfo("", true, true))) }
                    var isApplyClicked by remember { mutableStateOf(false) }

                    when (isApplyClicked) {
                        false -> {
                            Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))

                            Column(modifier = Modifier.padding(20.dp)) {

                                var uri by remember { mutableStateOf("") }
                                OutlinedTextField(
                                    placeholder = { Text("Enter URI") },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = uri,
                                    onValueChange = { text ->
                                        uri = text
                                    },
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                var username by remember { mutableStateOf("") }
                                OutlinedTextField(
                                    placeholder = { Text("Enter user name") },
                                    modifier = Modifier
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
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = password,
                                    onValueChange = { text ->
                                        password = text
                                    },
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(colors = ButtonDefaults.buttonColors(Color.White),
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(45.dp)
                                        .align(Alignment.End),
                                    onClick = {
                                        isApplyClicked = true
                                        Neo4jRepository(uri, username, password).apply {
                                            graphsList = dbGraphsInfo()
                                            close()
                                        }
                                    }) {
                                    Text("Apply")
                                }
                            }
                        }
                        else -> {
                            LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                items(graphsList) { currentDatabase ->
                                    Text(
                                        text = currentDatabase.name,
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .padding(5.dp)
                                            .clickable(
                                                onClick = {TODO()}
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        Screen.UndirectedGraph -> {
            undirectedGraphAnalyzerScreenView(undirectedGraphVM, BackButtonVM { screenState = Screen.Menu })
        }
        Screen.DirectedGraph -> {
            directedGraphAnalyzerScreenView(directedGraphVM, BackButtonVM { screenState = Screen.Menu })
        }
    }
}
