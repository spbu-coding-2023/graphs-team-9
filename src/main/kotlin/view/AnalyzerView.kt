package view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.BackButtonVM
import viewModel.GraphVM

@Composable
fun analyzerView(
    graphVM: GraphVM,
    backButtonVM: BackButtonVM,
) {
    var isShortestPathButtonClicked by remember { mutableStateOf(false) }
    var isCyclesButtonClicked by remember { mutableStateOf(false) }
    var isErrorInShortestPathOccurred by remember { mutableStateOf(false) }
    var isErrorInCyclesOccurred by remember { mutableStateOf(false) }
    var isSaveButtonClicked by remember { mutableStateOf(false) }
    var isOKButtonInShortestPathEnabled by remember { mutableStateOf(true) }
    var isOKButtonForCyclesEnabled by remember { mutableStateOf(true) }

    if (isErrorInShortestPathOccurred) alertDialogView({ isErrorInShortestPathOccurred = false }, "Error", "Path doesn't exist")

    if (isErrorInCyclesOccurred) alertDialogView({ isErrorInCyclesOccurred = false }, "Error", "No such vertex")

    when (isSaveButtonClicked) {
        true -> saveAsScreen({ isSaveButtonClicked = false }, graphVM)
        else -> {
            Column(
                modifier = Modifier.padding(10.dp).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(Modifier.weight(3f)) {
                    Button(
                        modifier = Modifier.weight(3f),
                        onClick = { isSaveButtonClicked = true },
                    ) {
                        Text(text = "save")
                    }

                    Spacer(Modifier.weight(3f))

                    Button(
                        modifier = Modifier.weight(3f),
                        onClick = (backButtonVM.onClick),
                    ) {
                        Text(text = "back")
                    }
                }

                Spacer(Modifier.weight(3f))

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.partitionAvailability,
                    onClick = (
                        {
                            graphVM.colorCommunities()
                            backButtonVM.saveCurrentAction()
                            val currentStronglyConnectedComponentsAvailability = graphVM.stronglyConnectedComponentsAvailability
                            if (currentStronglyConnectedComponentsAvailability) {
                                graphVM.stronglyConnectedComponentsAvailability = false
                            }
                            graphVM.partitionAvailability = false
                            backButtonVM.onClick = {
                                graphVM.resetVertexColors()
                                graphVM.stronglyConnectedComponentsAvailability = currentStronglyConnectedComponentsAvailability
                                graphVM.partitionAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "partition")
                }

                Spacer(Modifier.weight(2f))

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.keyVerticesAvailability,
                    onClick = (
                        {
                            graphVM.changeVerticesSizes()
                            backButtonVM.saveCurrentAction()
                            graphVM.keyVerticesAvailability = false
                            backButtonVM.onClick = {
                                graphVM.resetSizes()
                                graphVM.keyVerticesAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "key vertices")
                }

                Spacer(Modifier.weight(2f))

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.mfsAvailability,
                    onClick = (
                        {
                            graphVM.colorMSFEdges()
                            backButtonVM.saveCurrentAction()
                            val currentCyclesAvailability = graphVM.cyclesAvailability
                            if (currentCyclesAvailability) {
                                graphVM.cyclesAvailability = false
                            }
                            val currentBridgesAvailability = graphVM.bridgesAvailability
                            if (currentBridgesAvailability) {
                                graphVM.bridgesAvailability = false
                            }
                            val currentShortestPathAvailability = graphVM.shortestPathAvailability
                            if (currentShortestPathAvailability) {
                                graphVM.shortestPathAvailability = false
                            }
                            graphVM.mfsAvailability = false
                            backButtonVM.onClick = {
                                graphVM.removePaths()
                                graphVM.cyclesAvailability = currentCyclesAvailability
                                graphVM.bridgesAvailability = currentBridgesAvailability
                                graphVM.shortestPathAvailability = currentShortestPathAvailability
                                graphVM.mfsAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "minimum spanning forest")
                }

                Spacer(Modifier.weight(2f))

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.shortestPathAvailability,
                    onClick = (
                        {
                            val currentCyclesAvailability = graphVM.cyclesAvailability
                            if (currentCyclesAvailability) {
                                graphVM.cyclesAvailability = false
                            }
                            val currentBridgesAvailability = graphVM.bridgesAvailability
                            if (currentBridgesAvailability) {
                                graphVM.bridgesAvailability = false
                            }
                            val currentMfsAvailability = graphVM.mfsAvailability
                            if (currentMfsAvailability) {
                                graphVM.mfsAvailability = false
                            }

                            isShortestPathButtonClicked = true
                            graphVM.shortestPathAvailability = false
                            backButtonVM.saveCurrentAction()
                            backButtonVM.onClick = {
                                isShortestPathButtonClicked = false
                                graphVM.removePaths()
                                graphVM.cyclesAvailability = currentCyclesAvailability
                                graphVM.bridgesAvailability = currentBridgesAvailability
                                graphVM.mfsAvailability = currentMfsAvailability
                                graphVM.shortestPathAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "shortest path")
                }

                Spacer(Modifier.weight(2f))

                if (isShortestPathButtonClicked) {
                    Row {
                        var startVertex by remember { mutableStateOf("") }
                        OutlinedTextField(
                            placeholder = { Text("Start") },
                            value = startVertex,
                            onValueChange = { text ->
                                startVertex = text
                            },
                            modifier =
                                Modifier
                                    .weight(5f),
                        )

                        Spacer(Modifier.weight(1f))

                        var endVertex by remember { mutableStateOf("") }
                        OutlinedTextField(
                            placeholder = { Text("End") },
                            value = endVertex,
                            onValueChange = { text ->
                                endVertex = text
                            },
                            modifier =
                                Modifier
                                    .weight(5f),
                        )

                        Spacer(Modifier.weight(1f))

                        Button(
                            modifier = Modifier.weight(5f),
                            enabled = isOKButtonInShortestPathEnabled,
                            onClick = {
                                try {
                                    graphVM.colorShortestPath(startVertex, endVertex)
                                    backButtonVM.saveCurrentAction()
                                    isOKButtonInShortestPathEnabled = false

                                    backButtonVM.onClick = {
                                        isOKButtonInShortestPathEnabled = true
                                        startVertex = ""
                                        endVertex = ""
                                        graphVM.removePaths()
                                        backButtonVM.setSavedActionAsOnClick()
                                    }
                                } catch (e: IllegalArgumentException) {
                                    isErrorInShortestPathOccurred = true
                                }
                            },
                        ) {
                            Text("OK")
                        }
                    }

                    Spacer(Modifier.weight(2f))
                }

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.stronglyConnectedComponentsAvailability,
                    onClick = (
                        {
                            graphVM.colorStronglyConnectedComponents()
                            val currentPartitionAvailability = graphVM.partitionAvailability
                            if (currentPartitionAvailability) {
                                graphVM.partitionAvailability = false
                            }
                            graphVM.stronglyConnectedComponentsAvailability = false
                            backButtonVM.saveCurrentAction()
                            backButtonVM.onClick = {
                                graphVM.resetVertexColors()
                                graphVM.partitionAvailability = currentPartitionAvailability
                                graphVM.stronglyConnectedComponentsAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "strongly connected components")
                }

                Spacer(Modifier.weight(2f))

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.cyclesAvailability,
                    onClick = (
                        {
                            backButtonVM.saveCurrentAction()
                            val currentShortestPathAvailability = graphVM.shortestPathAvailability
                            if (currentShortestPathAvailability) {
                                graphVM.shortestPathAvailability = false
                                isShortestPathButtonClicked = false
                            }
                            val currentBridgesAvailability = graphVM.bridgesAvailability
                            if (currentBridgesAvailability) {
                                graphVM.bridgesAvailability = false
                            }
                            val currentMfsAvailability = graphVM.mfsAvailability
                            if (currentMfsAvailability) {
                                graphVM.mfsAvailability = false
                            }
                            graphVM.cyclesAvailability = false
                            isCyclesButtonClicked = true
                            backButtonVM.onClick = {
                                graphVM.removePaths()
                                graphVM.shortestPathAvailability = currentShortestPathAvailability
                                graphVM.bridgesAvailability = currentBridgesAvailability
                                graphVM.mfsAvailability = currentMfsAvailability
                                graphVM.cyclesAvailability = true
                                isCyclesButtonClicked = false
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "cycles")
                }

                Spacer(Modifier.weight(2f))

                if (isCyclesButtonClicked) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        var vertex by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = vertex,
                            placeholder = { Text("Vertex") },
                            onValueChange = { text ->
                                vertex = text
                            },
                            modifier = Modifier.weight(7f),
                        )

                        Spacer(Modifier.weight(1f))

                        Button(enabled = isOKButtonForCyclesEnabled, onClick = {
                            try {
                                graphVM.colorCycles(vertex)
                                backButtonVM.saveCurrentAction()
                                isOKButtonForCyclesEnabled = false
                                backButtonVM.onClick = {
                                    isOKButtonForCyclesEnabled = true
                                    vertex = ""
                                    graphVM.removePaths()
                                    backButtonVM.setSavedActionAsOnClick()
                                }
                            } catch (e: NoSuchElementException) {
                                isErrorInCyclesOccurred = true
                            }
                        }) {
                            Text("OK")
                        }
                    }

                    Spacer(Modifier.weight(2f))
                }

                Button(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    enabled = graphVM.bridgesAvailability,
                    onClick = (
                        {
                            graphVM.colorBridges()
                            backButtonVM.saveCurrentAction()
                            val currentShortestPathAvailability = graphVM.shortestPathAvailability
                            if (currentShortestPathAvailability) {
                                graphVM.shortestPathAvailability = false
                                isShortestPathButtonClicked = false
                            }
                            val currentCyclesAvailability = graphVM.cyclesAvailability
                            if (currentCyclesAvailability) {
                                graphVM.cyclesAvailability = false
                            }
                            val currentMfsAvailability = graphVM.mfsAvailability
                            if (currentMfsAvailability) {
                                graphVM.mfsAvailability = false
                            }
                            graphVM.bridgesAvailability = false
                            backButtonVM.onClick = {
                                graphVM.removePaths()
                                graphVM.shortestPathAvailability = currentShortestPathAvailability
                                graphVM.cyclesAvailability = currentCyclesAvailability
                                graphVM.mfsAvailability = currentMfsAvailability
                                graphVM.bridgesAvailability = true
                                backButtonVM.setSavedActionAsOnClick()
                            }
                        }
                    ),
                ) {
                    Text(text = "bridges")
                }

                Spacer(Modifier.weight(2f))

                OutlinedButton(
                    modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
                    onClick = ({ graphVM.resetLayout() }),
                ) {
                    Text("reset layout")
                }
            }
        }
    }
}
