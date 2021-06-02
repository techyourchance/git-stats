import cli.runGitStats
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        runGitStats(args)
    }
}
