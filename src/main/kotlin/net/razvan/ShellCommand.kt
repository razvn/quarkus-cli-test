package net.razvan

import java.io.BufferedReader

class ShellCommand {

    fun run(command: String, workingDir: String = ""): Response {
        val p = Runtime.getRuntime().exec(command)
        p.waitFor()
        val result = p.retriveResult()
        return Response(command, workingDir, result.output, result.exitCode, result.error)
    }


    private fun Process.retriveResult(): Result {
        val output = inputStream.bufferedReader().use(BufferedReader::readText)
        val exitCode = exitValue()
        val exitMessage = if (exitCode != 0) errorStream.bufferedReader().use(BufferedReader::readText) else ""
        return Result(output.trim(), exitCode, exitMessage.trim())
    }
    data class Result(val output: String = "", val exitCode: Int = 0, val error: String = "")
    data class Response(val command: String, val workingDir: String, val output: String = "", val exitCode: Int = 0, val error: String = "")
}


