package it.gasadvisor.gas_backend.client

import it.gasadvisor.gas_backend.util.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RequestCallback
import org.springframework.web.client.ResponseExtractor
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class RestClient @Autowired constructor(
    private val restTemplate: RestTemplate,
    private val retryTemplate: RetryTemplate
) {
    companion object : Log()

    init {
        val httpRequestFactory = BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
        val interceptor = RestClientInterceptor()
        val interceptors = ArrayList<ClientHttpRequestInterceptor>()
        interceptors.add(interceptor)

        this.restTemplate.requestFactory = httpRequestFactory
        this.restTemplate.interceptors = interceptors
    }

    fun <T> addMessageConverterToRestTemplate(messageConverter: HttpMessageConverter<T>) {
        restTemplate.messageConverters.add(messageConverter)
    }

    fun <T, V> executeRequestWithRetry(
        url: String,
        httpMethod: HttpMethod,
        httpEntity: HttpEntity<T>?,
        clazz: ParameterizedTypeReference<V>
    ): ResponseEntity<V> {
        return retryTemplate.execute<ResponseEntity<V>, Exception> { retryContext ->
            if (retryContext.lastThrowable != null) {
                log.info("Retry count: {}, exception: {}", retryContext.retryCount, retryContext.lastThrowable)
            }
            restTemplate.exchange(url, httpMethod, httpEntity, clazz)
        }
    }

    fun <T, V> executeRequestWithRetry(
        url: String,
        httpMethod: HttpMethod,
        httpEntity: HttpEntity<T>?,
        clazz: Class<V>
    ): ResponseEntity<V> {
        return retryTemplate.execute<ResponseEntity<V>, Exception> { retryContext ->
            if (retryContext.lastThrowable != null) {
                log.info("Retry count: {}, exception: {}", retryContext.retryCount, retryContext.lastThrowable)
            }
            restTemplate.exchange(url, httpMethod, httpEntity, clazz)
        }
    }

    fun <V> executeRequestWithRetry(url: String, httpMethod: HttpMethod, callback: RequestCallback, responseExtractor: ResponseExtractor<V>) : V {
        return retryTemplate.execute<V, Exception> { retryContext ->
            if (retryContext.lastThrowable != null) {
                log.info("Retry count: {}, exception: {}", retryContext.retryCount, retryContext.lastThrowable)
            }
            restTemplate.execute(url, httpMethod, callback, responseExtractor)
        }
    }

    fun <T, V> executeRequest(url: String, httpMethod: HttpMethod, httpEntity: HttpEntity<T>?, clazz: Class<V>): ResponseEntity<V>  {
        return restTemplate.exchange(url, httpMethod, httpEntity, clazz)
    }

    fun <T, V> executeRequest(uri: URI, httpMethod: HttpMethod, httpEntity: HttpEntity<T>?, clazz: Class<V>): ResponseEntity<V>  {
        return restTemplate.exchange(uri, httpMethod, httpEntity, clazz)
    }

    fun <T, V> executeRequestForList(url: String, httpMethod: HttpMethod, httpEntity: HttpEntity<T>?, typeRef: ParameterizedTypeReference<List<V>>) : ResponseEntity<List<V>> {
        return restTemplate.exchange(url, httpMethod, httpEntity, typeRef)
    }

    fun <T, V> executeRequestForList(uri: URI, httpMethod: HttpMethod, httpEntity: HttpEntity<T>?, typeRef: ParameterizedTypeReference<List<V>>) : ResponseEntity<List<V>> {
        return restTemplate.exchange(uri, httpMethod, httpEntity, typeRef)
    }

}
