package view

import androidx.compose.foundation.layout.*
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
    backButtonVM: BackButtonVM
) {
    Column(
        modifier = Modifier.padding(10.dp).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (Modifier.weight(3f)) {

            Button(
                modifier = Modifier.weight(3f),
                onClick = {TODO()}
            ) {
                Text(text = "save")
            }

            Spacer(Modifier.weight(3f))

            Button(
                modifier = Modifier.weight(3f),
                onClick = (backButtonVM.onClick)
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
                        backButtonVM.onClick = {
                            graphVM.resetVertexColors()
                            graphVM.stronglyConnectedComponentsAvailability = currentStronglyConnectedComponentsAvailability
                            backButtonVM.setSavedActionAsOnClick()
                        }

                    }
                    )
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
                        backButtonVM.onClick = {
                            graphVM.resetSizes()
                            backButtonVM.setSavedActionAsOnClick()
                        }
                    }
                    )
        ) {
            Text(text = "key vertices")
        }

        Spacer(Modifier.weight(2f))

        Button(
            modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
            enabled = graphVM.mfsAvailability,
            onClick = (
                    {
                        TODO()
//                        graphVM.changeVerticesSizes()
//                        backButtonVM.saveCurrentAction()
//                        backButtonVM.onClick = {
//                            graphVM.resetSizes()
//                            backButtonVM.setSavedActionAsOnClick()
//                        }
                    }
                    )
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
                        TODO()
                        graphVM.colorShortestPath("","")
                        backButtonVM.saveCurrentAction()
                        backButtonVM.onClick = {
                            graphVM.removePaths()
                            backButtonVM.setSavedActionAsOnClick()
                            graphVM.cyclesAvailability = currentCyclesAvailability
                            graphVM.bridgesAvailability = currentBridgesAvailability
                        }
                    }
                    )
        ) {
            Text(text = "shortest path")
        }

        Spacer(Modifier.weight(2f))

        Button(
            modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
            enabled = graphVM.stronglyConnectedComponentsAvailability,
            onClick = (
                    {
                        val currentPartitionAvailability = graphVM.partitionAvailability
                        if (currentPartitionAvailability) {
                            graphVM.partitionAvailability = false
                        }
                        graphVM.colorStronglyConnectedComponents()
                        backButtonVM.saveCurrentAction()
                        backButtonVM.onClick = {
                            graphVM.resetVertexColors()
                            backButtonVM.setSavedActionAsOnClick()
                            graphVM.partitionAvailability = currentPartitionAvailability
                        }
                    }
                    )
        ) {
            Text(text = "strongly connected components")
        }

        Spacer(Modifier.weight(2f))

        Button(
            modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
            enabled = graphVM.cyclesAvailability,
            onClick = (
                    {
                        val currentShortestPathAvailability = graphVM.shortestPathAvailability
                        if (currentShortestPathAvailability) {
                            graphVM.shortestPathAvailability = false
                        }
                        val currentBridgesAvailability = graphVM.bridgesAvailability
                        if (currentBridgesAvailability) {
                            graphVM.bridgesAvailability = false
                        }
                        TODO()
                        graphVM.colorCycles("")
                        backButtonVM.saveCurrentAction()
                        backButtonVM.onClick = {
                            graphVM.removePaths()
                            backButtonVM.setSavedActionAsOnClick()
                            graphVM.shortestPathAvailability = currentShortestPathAvailability
                            graphVM.bridgesAvailability = currentBridgesAvailability
                        }
                    }
                    )
        ) {
            Text(text = "cycles")
        }

        Spacer(Modifier.weight(2f))

        Button(
            modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
            enabled = graphVM.bridgesAvailability,
            onClick = (
                    {
                        val currentShortestPathAvailability = graphVM.shortestPathAvailability
                        if (currentShortestPathAvailability) {
                            graphVM.shortestPathAvailability = false
                        }
                        val currentCyclesAvailability = graphVM.cyclesAvailability
                        if (currentCyclesAvailability) {
                            graphVM.cyclesAvailability = false
                        }
                        graphVM.colorBridges()
                        backButtonVM.saveCurrentAction()
                        backButtonVM.onClick = {
                            graphVM.removePaths()
                            backButtonVM.setSavedActionAsOnClick()
                            graphVM.shortestPathAvailability = currentShortestPathAvailability
                            graphVM.cyclesAvailability = currentCyclesAvailability
                        }
                    }
                    )
        ) {
            Text(text = "bridges")
        }

        Spacer(Modifier.weight(2f))

        OutlinedButton(
            modifier = Modifier.weight(5f).align(Alignment.CenterHorizontally).fillMaxWidth(),
            onClick = ({graphVM.resetLayout()})
        ) {
            Text("reset layout")
        }
    }
}
