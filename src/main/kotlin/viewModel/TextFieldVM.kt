package viewModel

import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

class TextFieldVM(private var path: String ) {

    fun changePath(file: String, files: List<String> = listOf()): String {
        changePathEnterInDirectory(file, files)
        changePathAccordingToExitFromDirectory(file)
        return path
    }

    private fun changePathEnterInDirectory(file: String, files: List<String> = listOf()) {
        if (file.substringAfterLast("/") == "") {
            if (file.substringBeforeLast("/") in files && file.substringAfterLast(".") != "csv/") {
                path = "graphs/$file/"
            }
        }
    }

    private fun changePathAccordingToExitFromDirectory(file: String) {
        if (("graph/$file/").length < path.length) {
            path = path.substringBeforeLast("/").substringBeforeLast("/") + "/"
        }
    }

    fun addFilesToList(file: String): List<String> {
        val outputList = mutableListOf<String>()
        if ("//" !in "graphs/$file" && ".csv/" !in "graphs/$file") {
            Path(path).listDirectoryEntries("${file.substringAfterLast("/")}*").forEach {
                if (it.isDirectory() || (it.toString().substringAfterLast(".") == "csv")) {
                    outputList += it.toString().substringAfter("/")
                }
            }
        }
        return outputList
    }
}
