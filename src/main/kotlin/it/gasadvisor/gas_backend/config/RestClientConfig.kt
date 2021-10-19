package it.gasadvisor.gas_backend.config

import it.gasadvisor.gas_backend.util.logging.Log
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy
import org.springframework.retry.policy.NeverRetryPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets

@Configuration
class RestClientConfig constructor(
    @Value("\${connection.timeout:30000}") val connectionTimeout: Int,
    @Value("\${request.timeout:30000}") val requestTimeout: Int,
    @Value("\${read.timeout:30000}") val readTimeout: Int,
    @Value("\${retry.backoff.period:1000}") val retryBackoffPeriod: Long,
    @Value("\${retry.attempts:3}") val retryAttempts: Int
) {
    companion object : Log()

    class RestRetryPolicy constructor(private val retryAttempts: Int) : ExceptionClassifierRetryPolicy() {

        init {
            this.setExceptionClassifier lambda@{ ex ->
                val simpleRetryPolicy = SimpleRetryPolicy()
                simpleRetryPolicy.maxAttempts = retryAttempts
                if (ex is HttpStatusCodeException) {
                    val status = ex.statusCode
                    if (status.is5xxServerError) {
                        log.info("Http call failed. Retrying. Message: {}", ex.message)
                        return@lambda simpleRetryPolicy
                    }
                }
                log.info("Http call failed. Will not retry. Message: {}", ex.message)
                NeverRetryPolicy()
            }
        }
    }

    @Bean
    fun getRetryTemplate(): RetryTemplate {
        val template = RetryTemplate()
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = retryBackoffPeriod
        template.setBackOffPolicy(backOffPolicy)
        template.setRetryPolicy(RestRetryPolicy(retryAttempts))
        return template
    }

    @Bean
    fun getRestClient(): RestTemplate {
        return defaultRestClient()
    }

    private fun defaultRestClient(): RestTemplate {
        val httpClient = HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier())
            .build()
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        requestFactory.setReadTimeout(readTimeout)
        requestFactory.setConnectionRequestTimeout(requestTimeout)
        requestFactory.setConnectTimeout(connectionTimeout)

        val restTemplate = RestTemplate(requestFactory)
        restTemplate.messageConverters.add(StringHttpMessageConverter(StandardCharsets.UTF_8))
        return restTemplate
    }


}
