package view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import viewModel.GraphVM

@Composable
fun visibilitiesManagerView(graphVM: GraphVM) {
    Row {
        Spacer(Modifier.weight(0.1f))
        Row(Modifier.weight(0.9f)) {
            Switch(
                checked = graphVM.verticesDataVisibility,
                onCheckedChange = {
                    graphVM.changeVerticesDataVisibility()
                },
            )
            Text("data", Modifier.align(Alignment.CenterVertically))
        }
        Spacer(Modifier.weight(0.1f))
        Row(Modifier.weight(0.9f)) {
            Switch(
                checked = graphVM.weightVisibility,
                onCheckedChange = {
                    graphVM.changeWeightsVisibility()
                },
            )
            Text("weights", Modifier.align(Alignment.CenterVertically))
        }
        Spacer(Modifier.weight(0.1f))

        Row(Modifier.weight(0.9f)) {
            Switch(
                checked = graphVM.labelsVisibility,
                onCheckedChange = {
                    graphVM.changeLabelsVisibility()
                },
            )
            Text("labels", Modifier.align(Alignment.CenterVertically))
        }
        Spacer(Modifier.weight(0.1f))
    }
}
