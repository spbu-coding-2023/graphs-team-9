package viewModel

import view.ButtonPressed

class MainButtonVM(private val currentState: ButtonPressed) {
    fun onClick(newState: ButtonPressed): ButtonPressed {
        when (newState) {
            ButtonPressed.ChooseFile -> {
                return when (currentState) {
                    ButtonPressed.ChooseFile -> ButtonPressed.None
                    else -> ButtonPressed.ChooseFile
                }
            }
            ButtonPressed.SQLlite -> {
                return when (currentState) {
                    ButtonPressed.SQLlite -> ButtonPressed.None
                    else -> ButtonPressed.SQLlite
                }
            }
            else -> {
                return when (currentState) {
                    ButtonPressed.Neo4j -> ButtonPressed.None
                    else -> ButtonPressed.Neo4j
                }
            }
        }
    }
}
