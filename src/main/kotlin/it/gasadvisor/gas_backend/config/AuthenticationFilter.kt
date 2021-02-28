package it.gasadvisor.gas_backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import it.gasadvisor.gas_backend.api.auth.contract.AuthenticationRequest
import it.gasadvisor.gas_backend.util.JwtHelper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val mapper: ObjectMapper,
    private val jwtHelper: JwtHelper
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val authRequest = mapper.readValue(request!!.inputStream, AuthenticationRequest::class.java)
        val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        return authManager.authenticate(authToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        response!!.addHeader("Authorization", jwtHelper.createJwt(authResult!!.name, authResult.authorities))
    }
}
