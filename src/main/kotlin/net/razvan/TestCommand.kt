package net.razvan

import io.quarkus.picocli.runtime.annotations.TopCommand
import picocli.CommandLine
import javax.enterprise.context.Dependent

@TopCommand
@CommandLine.Command(name = "quarkus-cl")
class TestCommand(private val service: RunCommandsService) : Runnable {
    @CommandLine.Option(names = ["-s", "--shell"], description = ["shell command to run"], defaultValue = "")
    var shellCmd: String = ""

    @CommandLine.Option(names = ["-g", "--get"], description = ["url get comman to grab"], defaultValue = "")
    var getUrl: String = ""

    @CommandLine.Option(names = ["-p", "--post"], description = ["url post command send"], defaultValue = "")
    var postUrl: String = ""

    @CommandLine.Option(
        names = ["-h", "--headers"],
        description = ["headers to pass (format: Name1: Value1, Name2: Value2)"],
        defaultValue = ""
    )
    var headers: String = ""


    @CommandLine.Option(names = ["-d", "--data"], description = ["What post data to send ?"], defaultValue = "")
    var postData: String = ""


    override fun run() {

        service.runCommands(shellCmd, getUrl, postUrl, headers, postData)


        // val apiGet = ApiCommand().get("https://jsonplaceholder.typicode.com/todos/1")

        //        val apiPost = ApiCommand().post(
        //           "https://jsonplaceholder.typicode.com/posts",
        //           mapOf("Content-type" to "application/json"),
        //           """
        //              {
        //                  "title": "foo",
        //                  "body": "bar",
        //                  "userId": 1
        //              }
        //          """.trimIndent()
        //      )
    }
}

@Dependent
class RunCommandsService {
    private val shellCommand = ShellCommand()
    private val apiCommand = ApiCommand()

    fun runCommands(shell: String, get: String, post: String, headers: String, data: String) {
        if (shell.isNotEmpty()) {
            runShellCommand(shell)
        }

        val apiHeaders = formatHeaders(headers)

        if (get.isNotEmpty()) {
            runApiGet(get, apiHeaders)
        }

        if (post.isNotEmpty()) {
            runApiPost(post, apiHeaders, data)
        }
    }

    private fun formatHeaders(headers: String): Map<String, String> {
        return if (headers.isBlank()) mapOf() else {
            headers.split(",").mapNotNull {
                val header = it.split(":")
                if (header.size == 2) {
                    header[0] to header[1]
                } else null
            }.toMap()
        }
    }

    private fun runShellCommand(cmd: String) {
        val response = shellCommand.run(cmd)
        println("Exitcode: ${response.exitCode}")
        println("Result: ${response.output}")
        println("Error: ${response.error}")
    }

    private fun runApiGet(url: String, headers: Map<String, String>) {
        val response = apiCommand.get(url, headers)
        println("Api Get Response code: ${response.first}")
        println("Api Get Response code: ${response.second}")
    }

    private fun runApiPost(url: String, headers: Map<String, String>, data: String) {
        val response = apiCommand.post(url, headers, data)
        println("Api Post Response code: ${response.first}")
        println("Api Post Response body: ${response.second}")
    }
}