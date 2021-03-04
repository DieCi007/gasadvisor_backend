package it.gasadvisor.gas_backend.config

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationEntrypointConfig : BasicAuthenticationEntryPoint() {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        response.sendError(
            HttpStatus.UNAUTHORIZED.value(),
            "You are not authorized to proceed"
        )
    }
}
