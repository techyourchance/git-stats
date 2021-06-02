package cli

import cli.CliConfig.GIT
import io.ExecuteCommandOptions
import io.executeCommandAndCaptureOutput
import io.fileIsReadable
import io.findExecutable

var options = ExecuteCommandOptions(directory = ".", abortOnError = true, redirectStderr = true, trim = true)

/***
 * CUSTOMIZE_ME: this file is all specific to git-standup and can be deleted once understood
 */
suspend fun runGitStats(args: Array<String>) {

    val jsPackage = "/build/js/packages/kotlin-cli-starter"
    val pwd = executeCommandAndCaptureOutput(listOf("pwd"), options)
    if (pwd.contains(jsPackage)) {
        options = options.copy(directory = pwd.removeSuffix(jsPackage))
    }
    GIT = findExecutable(GIT)
    val command = CliCommand()

    command.main(args)

    if (fileIsReadable(".git")) {
        val fileStats = parseCommits()
        printStats(fileStats)
    } else {
        println("git-stats must be run inside a git repository")
    }
}

fun printStats(fileStats: Map<String, Int>) {
    val fileStatsSorted = fileStats.entries.sortedBy { it.value }
    fileStatsSorted.forEach {
        println("${it.value} : ${it.key}")
    }
}

suspend fun parseCommits(): Map<String, Int> {
    val gitLogOutput: String = executeCommandAndCaptureOutput("git log  --numstat --name-only".split(" "), options)
    val commits = parseGitlogOutput(gitLogOutput)

    val fileStats = mutableMapOf<String, Int>()
    commits.forEach { commit ->
        parseCommitFiles(commit, fileStats)
    }

    return fileStats
}

fun parseGitlogOutput(gitLogOutput: String): List<GitCommit> {
    val lines = gitLogOutput.lines()
    val indexes = lines.indices
        .filter { lines[it].startsWith("commit") } + listOf(lines.size)

    val commits = indexes.zipWithNext().map { (start, end) ->
        GitCommit(lines.slice(start until end).joinToString(separator = "\n"))
    }
    return commits
}

suspend fun parseCommitFiles(commits: GitCommit, fileStats: MutableMap<String, Int>) {
    val changedFiles = commits.files
    changedFiles.forEach { fileName ->
        fileStats[fileName] = fileStats.getOrPut(fileName) { 0 } + 1
    }
}


data class GitCommit(
    val commit: String,
    val branches: String,
    val author: String,
    val date: String,
    val message: String,
    val files: List<String>
) {
    companion object {
        operator fun invoke(text: String): GitCommit {
            val lines = text.lines()
            val first = lines.first().removePrefix("commit ")
            val commit = first.substringBefore(" ")
            val branches = first.substringAfter(" ").removePrefix("(").removeSuffix(")")
            val author = lines.first { it.startsWith("Author") }.removePrefix("Author:").trim()
            val date = lines.first { it.startsWith("Date") }.removePrefix("Date:").trim()
            val message = lines.filter { it.startsWith("    ") }.map { it.trim() }.joinToString(separator = "\n")
            val startIndex = lines.indexOfLast { it.startsWith("    ") } + 1
            val files = lines.subList(startIndex, lines.size)
                .filter { it.isNotBlank() }
            return GitCommit(commit, branches, author, date, message, files)
        }
    }

}