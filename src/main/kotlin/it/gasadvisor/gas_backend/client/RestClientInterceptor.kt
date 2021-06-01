package it.gasadvisor.gas_backend.client

import it.gasadvisor.gas_backend.util.Log
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

class RestClientInterceptor : ClientHttpRequestInterceptor {
    companion object : Log()

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        logRequest(request, body)
        val response = execution.execute(request, body)
        logResponse(response)
        return response
    }

    private fun logRequest(request: HttpRequest, body: ByteArray) {
        log.trace("Rest client request: {} {} {}", request.method, request.uri, String(body, StandardCharsets.UTF_8))
    }

    private fun logResponse(response: ClientHttpResponse) {
        log.trace("Rest client response: {} {}", response.statusCode, StreamUtils.copyToString(response.body, StandardCharsets.UTF_8))
    }


}
