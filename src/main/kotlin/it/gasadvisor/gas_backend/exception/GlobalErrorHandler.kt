package it.gasadvisor.gas_backend.exception

import com.google.common.base.Strings
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalErrorHandler {

    val log: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun otherExceptions(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        val annotation = AnnotationUtils.findAnnotation(
            ex.javaClass,
            ResponseStatus::class.java
        )
        return if (null != annotation) {
            fromException(ex, request, annotation.code)
        } else unhandledExceptions(ex, request)
    }

    @ExceptionHandler(value = [HttpMediaTypeNotSupportedException::class])
    @ResponseBody
    fun mediaTypeNotSupported(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        return fromException(ex, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(
        value = [HttpMessageNotReadableException::class, MissingServletRequestParameterException::class,
            MethodArgumentTypeMismatchException::class,
            MethodArgumentNotValidException::class, ConstraintViolationException::class]
    )
    @ResponseBody
    fun badRequestException(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        log.info(ex.message)
        return fromException(ex, request, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [ExpiredJwtException::class, UnauthorizedException::class,
    MalformedJwtException::class, HttpClientErrorException.Unauthorized::class, SignatureException::class])
    @ResponseBody
    fun jwtException(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        return fromException(ex, request, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    @ResponseBody
    fun methodNotSupported(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        return fromException(ex, request, HttpStatus.METHOD_NOT_ALLOWED)
    }

    private fun fromException(
        ex: Exception,
        request: WebRequest,
        httpStatus: HttpStatus
    ): ResponseEntity<Map<String, Any>> {
        val result = fromException(httpStatus, ex, request)
        return ResponseEntity(result, httpStatus)
    }

    private fun unhandledExceptions(ex: Exception, request: WebRequest): ResponseEntity<Map<String, Any>> {
        log.error(ex.message, ex)
        val result = maskingException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "We cannot handle your request now. Please try again later",
            request
        )
        return ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        private const val TS_KEY = "timeStamp"
        private const val STATUS_KEY = "status"
        private const val ERROR_KEY = "error"
        private const val EXCEPTION_KEY = "exception"
        private const val MESSAGE_KEY = "message"
        private const val PATH_KEY = "path"
        private fun fromException(httpStatus: HttpStatus, ex: Exception, request: WebRequest): Map<String, Any> {
            val result: MutableMap<String, Any> = HashMap()
            result[TS_KEY] = System.currentTimeMillis()
            result[STATUS_KEY] = httpStatus.value()
            result[ERROR_KEY] = httpStatus.reasonPhrase
            result[EXCEPTION_KEY] = ex.javaClass.simpleName
            result[MESSAGE_KEY] = ex.message ?: ""
            result[PATH_KEY] = getPath(request)
            return result
        }

        private fun maskingException(httpStatus: HttpStatus, message: String, request: WebRequest): Map<String, Any> {
            val result: MutableMap<String, Any> = HashMap()
            result[TS_KEY] = System.currentTimeMillis()
            result[STATUS_KEY] = httpStatus.value()
            result[ERROR_KEY] = httpStatus.reasonPhrase
            result[EXCEPTION_KEY] = "Undisclosed"
            result[MESSAGE_KEY] = message
            result[PATH_KEY] = getPath(request)
            return result
        }

        private fun getPath(request: WebRequest): String {
            var path = request.getDescription(false)
            if (!Strings.isNullOrEmpty(path)) {
                path = path.replaceFirst("uri=".toRegex(), "")
            }
            return path
        }
    }
}
