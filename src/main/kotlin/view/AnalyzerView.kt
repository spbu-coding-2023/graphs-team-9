package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
    Column(
        modifier = Modifier.padding(10.dp).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.weight(3f)) {
            Button(
                modifier = Modifier.weight(3f),
                onClick = { TODO() },
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
                    TODO()
                    graphVM.colorShortestPath("", "")
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
                    graphVM.shortestPathAvailability = false
                    backButtonVM.saveCurrentAction()
                    backButtonVM.onClick = {
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
                    TODO()
                    graphVM.colorCycles("")
                    backButtonVM.saveCurrentAction()
                    val currentShortestPathAvailability = graphVM.shortestPathAvailability
                    if (currentShortestPathAvailability) {
                        graphVM.shortestPathAvailability = false
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
                    backButtonVM.onClick = {
                        graphVM.removePaths()
                        graphVM.shortestPathAvailability = currentShortestPathAvailability
                        graphVM.bridgesAvailability = currentBridgesAvailability
                        graphVM.mfsAvailability = currentMfsAvailability
                        graphVM.cyclesAvailability = true
                        backButtonVM.setSavedActionAsOnClick()
                    }
                }
                ),
        ) {
            Text(text = "cycles")
        }

        Spacer(Modifier.weight(2f))

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
