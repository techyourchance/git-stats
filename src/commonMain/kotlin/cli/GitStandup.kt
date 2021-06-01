package cli

import cli.CliConfig.CURRENT_GIT_USER
import cli.CliConfig.FIND
import cli.CliConfig.GIT
import io.*

var options = ExecuteCommandOptions(directory = ".", abortOnError = true, redirectStderr = true, trim = true)

/***
 * CUSTOMIZE_ME: this file is all specific to git-standup and can be deleted once understood
 */
suspend fun runGitStandup(args: Array<String>) {

    val jsPackage = "/build/js/packages/kotlin-cli-starter"
    val pwd = executeCommandAndCaptureOutput(listOf("pwd"), options)
    if (pwd.contains(jsPackage)) {
        options = options.copy(directory = pwd.removeSuffix(jsPackage))
    }
    GIT = findExecutable(GIT)
    FIND = findExecutable(FIND)
    CURRENT_GIT_USER = executeCommandAndCaptureOutput(listOf(GIT, "config", "user.name"), options)
    val command = CliCommand()
    val currentDirectory = executeCommandAndCaptureOutput(listOf("pwd"), options).trim()

    command.main(args)

    val fileStats = parseCommits()
    printStats(fileStats)

}

fun printStats(fileStats: Map<String, Int>) {
    val fileStatsSorted = fileStats.entries.sortedBy { it.value }
    fileStatsSorted.forEach {
        println("${it.value} : ${it.key}")
    }
}

suspend fun parseCommits(): Map<String, Int> {
    val commits = executeCommandAndCaptureOutput("git log --pretty=format:%h".split(" "), options)
    val commitsList = commits.lines()

    val fileStats = mutableMapOf<String, Int>()
    commitsList.forEach {
        val commits = executeCommandAndCaptureOutput("git diff-tree --no-commit-id --name-only -r $it".split(" "), options)
        parseCommitFiles(commits, fileStats)
    }

    return fileStats
}

suspend fun parseCommitFiles(commits: String, fileStats: MutableMap<String, Int>) {
    val changedFiles = commits.lines().filter { it.isNotBlank() }
    changedFiles.forEach { fileName ->
        fileStats[fileName] = fileStats.getOrPut(fileName) { 0 } + 1
    }
}

