package cli

import cli.CliConfig.COMMAND_NAME
import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import io.fileIsReadable

/**
 * git stats
 */
class CliCommand : CliktCommand(
    help = """
       Find out which files of a git repository are most often modified.
    """.trimIndent(),
    epilog = """""".trimIndent(),
    name = COMMAND_NAME
) {
    init {
        completionOption()
    }

    override fun run() {

    }
}

