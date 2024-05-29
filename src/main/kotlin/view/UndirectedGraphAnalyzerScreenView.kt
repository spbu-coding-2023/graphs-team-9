package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewModel.BackButtonVM
import viewModel.UndirectedGraphVM

@Composable
fun undirectedGraphAnalyzerScreenView(
    graphVM: UndirectedGraphVM,
    backButtonVM: BackButtonVM,
) {
    Column {
        Row(Modifier.weight(0.95f)) {
            Box(
                Modifier
                    .weight(20f),
            ) {
                graphVM.layout()
                undirectedGraphView(graphVM)
            }

            Box(Modifier.weight(5f)) {
                analyzerView(graphVM, backButtonVM)
            }
        }
        Box(Modifier.weight(0.05f)) {
            visibilitiesManagerView(graphVM)
        }
    }
}
