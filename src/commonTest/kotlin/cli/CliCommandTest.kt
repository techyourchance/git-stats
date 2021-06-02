package cli

import kotlin.test.Test
import kotlin.test.assertEquals


class CliCommandTest {
    val expected = GitCommit(
        commit = "db1ab6e5a5748a8eca2665a21d0e2d721a32ec8d",
        branches = "HEAD -> main, origin/main, origin/HEAD",
        author = "Jean-Michel Fayard <jmfayard@gmail.com>",
        date = "Tue Jun 1 19:49:02 2021 +0200",
        message = "Handle CUSTOMIZE_ME comments",
        files = listOf("README.md", "bin/git-standup", "bin/git-stats", "installer.sh", "settings.gradle.kts")
    )

    @Test fun parseACommit() {
        assertEquals(
            expected,
            GitCommit(commit)
        )
    }

    @Test fun parseThreeCommits() {
        val gitLogOutput = "$commit\n$commit\n$commit"
        val actual = parseGitlogOutput(gitLogOutput)
        assertEquals(
            expected = listOf(expected, expected, expected),
            actual = actual
        )
    }
}

val commit = """
        commit db1ab6e5a5748a8eca2665a21d0e2d721a32ec8d (HEAD -> main, origin/main, origin/HEAD)
        Author: Jean-Michel Fayard <jmfayard@gmail.com>
        Date:   Tue Jun 1 19:49:02 2021 +0200

            Handle CUSTOMIZE_ME comments

        README.md
        bin/git-standup
        bin/git-stats
        installer.sh
        settings.gradle.kts
    """.trimIndent()


