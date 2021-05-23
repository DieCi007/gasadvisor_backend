package it.gasadvisor.gas_backend.config

import it.gasadvisor.gas_backend.util.JwtHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import java.util.function.Predicate
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter constructor(
    private val jwtHelper: JwtHelper
) : OncePerRequestFilter() {
    private val log: Logger = LoggerFactory.getLogger(AuthorizationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = retrieveToken(request)

        if (token.isEmpty) {
            filterChain.doFilter(request, response)
            return
        }

        val claims = try {
            jwtHelper.readJwt(token.get())
        } catch (e: Exception) {
            log.info("invalid token, request from ${request.requestURL}")
            filterChain.doFilter(request, response)
            return
        }

        val username = claims.subject
        val jwtAuthorities = claims[JwtHelper.AUTHORITIES_CLAIM] as List<*>
        val authorities = jwtAuthorities.map { m -> SimpleGrantedAuthority(m as String) }.toSet()

        val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
        log.info("incoming request from $username")
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

    private fun retrieveToken(request: HttpServletRequest): Optional<String> {
        return Optional.ofNullable(request.getHeader("Authorization"))
            .filter(Predicate.not(String::isNullOrBlank))
    }
}
