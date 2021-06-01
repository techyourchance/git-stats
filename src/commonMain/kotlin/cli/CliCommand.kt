package cli

import cli.CliConfig.COMMAND_NAME
import cli.CliConfig.CURRENT_GIT_USER
import cli.CliConfig.FIND
import cli.CliConfig.GIT
import cli.CliConfig.GIT_STANDUP_WHITELIST
import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import io.fileIsReadable
import io.readAllText

/**
 * git stats
 */
class CliCommand : CliktCommand(
    help = """
       Recall what you did on the last working day ..or be nosy and find what someone else did.
    """.trimIndent(),
    epilog = """
        Repositories will be searched in the current directory
        unless a file `.git-standup-whitelist` is found that contains repository paths.

        Examples:
            $COMMAND_NAME -a "John Doe" -w "MON-FRI" -m 3
    """.trimIndent(),
    name = COMMAND_NAME
) {
    init {
        completionOption()
    }
    override fun run() {

    }
}

