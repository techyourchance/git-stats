package cli

import cli.CliConfig.CURRENT_GIT_USER
import cli.CliConfig.FIND
import cli.CliConfig.GIT
import io.*
import kotlinx.serialization.Serializable

/***
 * CUSTOMIZE_ME: this file is all specific to git-standup and can be deleted once understood
 */
suspend fun runGitStandup(args: Array<String>) {
    var options = ExecuteCommandOptions(directory = ".", abortOnError = true, redirectStderr = true, trim = true)

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

    val commits = executeCommandAndCaptureOutput("git log --pretty=format:%h".split(" "), options)
    val commitsList = commits.lines()

    commitsList.forEach {
        println(it)
    }
}

