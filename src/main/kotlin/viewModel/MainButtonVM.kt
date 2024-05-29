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
            ButtonPressed.PostgreSQL -> {
                return when (currentState) {
                    ButtonPressed.PostgreSQL -> ButtonPressed.None
                    else -> ButtonPressed.PostgreSQL
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
