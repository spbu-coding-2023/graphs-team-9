package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import viewModel.BackButtonVM
import viewModel.DirectedGraphVM

@Composable
fun directedGraphAnalyzerScreenView(
    graphVM: DirectedGraphVM,
    backButtonVM: BackButtonVM
) {
    Row(Modifier) {

        Box(
            Modifier
                .weight(20f)
        ){
            graphVM.layout()
            directedGraphView(graphVM)
        }

        Box(Modifier.weight(5f)) {
            analyzerView(graphVM, backButtonVM)
        }
    }
}
