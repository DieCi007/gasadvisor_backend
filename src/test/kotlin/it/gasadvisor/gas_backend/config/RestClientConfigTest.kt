package it.gasadvisor.gas_backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import it.gasadvisor.gas_backend.client.RestClient
import it.gasadvisor.gas_backend.util.Log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.retry.support.RetryTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate


@SpringBootTest(classes = [RetryTemplate::class, RestTemplate::class, ObjectMapper::class, RestClient::class])
@TestPropertySource("classpath:application-test.properties")
internal class RestClientConfigTest @Autowired constructor(
    private val restClient: RestClient,
    private val mapper: ObjectMapper
) {
    companion object : Log()
    data class TestResponse(val response: String)

    private lateinit var wireMockRule: WireMockRule
    private lateinit var host: String
    private val testResponse = TestResponse("hello world")

    @BeforeEach
    fun setUp() {
        wireMockRule = WireMockRule(WireMockConfiguration.options().dynamicPort())
        wireMockRule.start()

        host = "http://localhost:${wireMockRule.port()}"
        stubForStatus(200)
        stubForStatus(400)
        stubForStatus(500)
    }

    @Test
    fun executeRequest200() {
        val body: HttpEntity<Any>? = null
        val response: ResponseEntity<TestResponse> =
            restClient.executeRequestWithRetry("$host/200/test", HttpMethod.GET, body, TestResponse::class.java)
        assertEquals(response.statusCodeValue, 200)
        assertEquals(response.body, testResponse)
    }

    @Test
    fun executeRequest400() {
        val body: HttpEntity<Any>? = null
        assertThrows(
            HttpClientErrorException::class.java
        ) { restClient.executeRequestWithRetry("$host/400/test", HttpMethod.GET, body, TestResponse::class.java) }
    }

    @Test
    fun executeRequest500() {
        val body: HttpEntity<Any>? = null
        assertThrows(HttpServerErrorException.InternalServerError::class.java)
        { restClient.executeRequestWithRetry("$host/500/test", HttpMethod.GET, body, TestResponse::class.java) }
    }


    private fun stubForStatus(statusCode: Int) {
        wireMockRule.stubFor(
            get(urlEqualTo("/$statusCode/test"))
                .willReturn(
                    aResponse()
                        .withBody(mapper.writeValueAsBytes(testResponse))
                        .withHeader("Content-Type", "application/json")
                        .withStatus(statusCode)
                )
        )
    }
}
