package viewModel

import androidx.compose.runtime.mutableStateOf
import java.util.Stack

class BackButtonVM(
    onClick: () -> Unit,
) {
    private val onClickState = mutableStateOf(onClick)
    var onClick: () -> Unit
        get() {
            return onClickState.value
        }

        set(actions) {
            onClickState.value = actions
        }
    private val onClickActions = Stack<() -> Unit>()

    fun saveCurrentAction() {
        onClickActions.push(onClickState.value)
    }

    fun setSavedActionAsOnClick() {
        if (!onClickActions.empty()) {
            onClickState.value = onClickActions.pop()
            return
        }
        onClickState.value = {}
    }
}
