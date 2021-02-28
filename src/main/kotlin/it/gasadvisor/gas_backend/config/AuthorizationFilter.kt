package it.gasadvisor.gas_backend.config

import it.gasadvisor.gas_backend.util.JwtHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter constructor(
    private val jwtHelper: JwtHelper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val log: Logger = LoggerFactory.getLogger(AuthorizationFilter::class.java)
        val token = request.getHeader("Authorization")
        val claims = jwtHelper.readJwt(token)
        val username = claims.subject
        val jwtAuthorities = claims["authority"] as List<Map<String, String>>
        val authorities = jwtAuthorities.map { m -> SimpleGrantedAuthority(m["authority"]) }.toSet()
        val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
        log.info("incoming request from $username")
        authorities.forEach { a -> println(a) }
        SecurityContextHolder.getContext().authentication = authentication
    }

}
