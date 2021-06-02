package cli

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CliConfig {
    val COMMAND_NAME = "git-stats"

    var GIT = "git"
}