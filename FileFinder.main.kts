import java.io.File

private val pwd = File("")
private val S = "    "
private val fileType by lazy { args[0] }
private val verbose: Boolean
    get() = args.contains("v")
private val showHidden: Boolean
    get() = args.contains("h")

main()

fun pwd(): String {
    try {
        val filePath = File("").absoluteFile
        return "This script is being called from the following directory: $filePath"
    } catch (e: SecurityException) {
        println("A required system property value cannot be accessed.")
        throw e
    }
}

fun main() {
    if (args.isEmpty()) {
        printManualUsage()
        return
    }
    printAllFilesInFolder()
}

fun printManualUsage() {
    println(
        """
FileFinder(1)                   6879756e General Commands Manual                  FileFinder(1)
NAME
    FileFinder -- finds all files of a specific types in the directory and all its subdirectories
    
SYNOPSIS
    kotlinc -script FileFinder.main.kts -f -v -h"
    
DESCRIPTION
    The file finder, as the name suggests, helps find files of a particular type.
    
    The following options are available:
    
    -f    The file type e.g. jpg
    
    -v    Lists all files (default is 5)
    
    -h    Shows hidden files (default is off)
    
    """
    )

}

fun printAllFilesInFolder() {
    val filesList = pwd.absoluteFile.listFiles()
    println("Searching in the follow directory: ${pwd.absoluteFile}")

    val sb = StringBuilder()

    filesList?.filter { it.isWhatWeAreLookingFor() }?.forEach { sb.appendLine("$S$it") }

    val result = when {
        sb.isEmpty() -> "No files of the type: $fileType exists"
        (verbose || sb.lines().size < 5) -> sb.lines().sorted().joinToString("\n")
        else -> {
            sb.lines().take(5).sorted().joinToString("\n") +
                    "\n$S...\n" +
                    "As there were a lot of results, we only listed the first 5!\n" +
                    "Tip: Use the -v flag to list all results!"
        }
    }
    println(result)
}

fun File.isWhatWeAreLookingFor(): Boolean {
    if (!this.isHidden) return name.endsWith(fileType)

    return this.endsWith(fileType) && showHidden
}
