import java.io.File

private val pwd = File("")
private val S = "    "

// Related to arguments
private val legalArguments = setOf("man", "v", "h")
private lateinit var illegalArgument: String
private val fileType by lazy { args[0] }
private val verbose: Boolean
    get() = args.contains("v")
private val showHidden: Boolean
    get() = args.contains("h")

fun main() {
    when {
        args.isEmpty() || args[0] == "man" -> printManualUsage()

        args.containsIllegalArgument() -> printIllegalArgumentMessage()

        else -> printAllFilesInFolder()
    }
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

fun Array<String>.containsIllegalArgument(): Boolean {
    for (i in 1 until this.size) {
        if (this[i] !in legalArguments) {
            illegalArgument = this[i]
            return true
        }
    }
    return false
}

fun printIllegalArgumentMessage() {
    println("Sorry, we could not recognise the command $illegalArgument.\n" +
            "Please run kotlinc -script FileFinder.main.kts man to find out possible use cases.")
}

fun printAllFilesInFolder() {
    val filesList = pwd.absoluteFile.listFiles()
    println("Searching in the follow directory: ${pwd.absoluteFile}")

    val sb = StringBuilder()

    filesList?.filter { it.isWhatWeAreLookingFor() }?.forEach { sb.appendLine("$S$it") }

    val result = when {
        sb.isEmpty() -> "No files of the type: $fileType exists"

        verbose || sb.lines().size < 5 -> sb.lines().sorted().joinToString("\n$S")

        else -> {
"""
${sb.lines().take(5).sorted().joinToString("\n")}
$S...

As there were a lot of results (${sb.lines().size}), we only listed the first 5!
Tip: Use the -v flag to list all results!
"""
        }
    }
    println(result)
}

fun File.isWhatWeAreLookingFor(): Boolean {
    if (!this.isHidden) return name.endsWith(fileType)

    return this.endsWith(fileType) && showHidden
}

main()