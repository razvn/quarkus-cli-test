package net.razvan

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ApiCommand {
    final val executorService = Executors.newFixedThreadPool(5)
    val httpClient = HttpClient.newBuilder()
        .executor(executorService)
        .version(HttpClient.Version.HTTP_2)
        .build()

    fun get(url: String, headers: Map<String, String>? = null): Pair<Int, String> {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
        headers?.forEach {
            request.header(it.key, it.value)
        }

        val response = httpClient
            .sendAsync(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            )
            .toCompletableFuture()
            .join()

        return getResponse(response)
    }

    fun post(url: String, headers: Map<String, String>? = null, body: String): Pair<Int, String> {
        val request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .uri(URI.create(url))
        headers?.forEach {
            request.header(it.key, it.value)
        }

        val response = httpClient
            .sendAsync(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            )
            .toCompletableFuture()
            .join()

        return getResponse(response)
    }


    private fun getResponse(response: HttpResponse<String>): Pair<Int, String> {
        val status = response.statusCode()
        val respBody: String = response.body().trim()

        return status to respBody
    }
}