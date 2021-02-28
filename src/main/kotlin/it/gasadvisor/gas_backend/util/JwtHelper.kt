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
    val days: Long = 10

    @Value("\${jwt.secure.key}")
    val key: String = ""

    fun createJwt(user: String, authority: MutableCollection<out GrantedAuthority>): String {
        return Jwts.builder().setSubject(user)
            .claim("authority", authority)
            .setIssuedAt(Date())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(days))))
            .signWith(Keys.hmacShaKeyFor(key.toByteArray())).compact()
    }

    fun readJwt(token: String): Claims {
        val claimsJws = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(key.toByteArray()))
            .build().parseClaimsJws(token)
        return claimsJws.body
    }

}
