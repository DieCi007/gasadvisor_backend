package it.gasadvisor.gas_backend.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.GrantedAuthority
import java.time.Duration
import java.time.Instant
import java.util.*

@Configuration
class JwtHelper {

    @Value("\${jwt.expiration.days}")
    private val jwtExpirationDays: Long = 1

    @Value("\${jwt.refresh.expiration.days}")
    private val jwtRefreshExpirationDays: Long = 30

    @Value("\${jwt.secure.key}")
    private val key: String = ""

    fun createPrimaryJwt(user: String, authorities: Collection<GrantedAuthority>): String {
        return createJwt(user, authorities, jwtExpirationDays)
    }

    fun createRefreshJwt(user: String, authorities: Collection<GrantedAuthority>): String {
        return createJwt(user, authorities, jwtRefreshExpirationDays)
    }

    private fun createJwt(
        user: String,
        authorities: Collection<GrantedAuthority>,
        expirationInDays: Long
    ): String {
        return Jwts.builder().setSubject(user)
            .claim(AUTHORITIES_CLAIM, authorities.map { grantedAuthority -> grantedAuthority.authority })
            .setIssuedAt(Date())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(expirationInDays))))
            .signWith(Keys.hmacShaKeyFor(key.toByteArray())).compact()
    }

    fun readJwt(token: String): Claims {
        val claimsJws = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(key.toByteArray()))
            .build().parseClaimsJws(token)
        return claimsJws.body
    }

    companion object {
        const val AUTHORITIES_CLAIM = "authorities"
    }
}
