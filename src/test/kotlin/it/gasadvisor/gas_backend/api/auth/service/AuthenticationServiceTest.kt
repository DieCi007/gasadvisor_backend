package it.gasadvisor.gas_backend.api.auth.service

import it.gasadvisor.gas_backend.api.auth.contract.RefreshTokenRequest
import it.gasadvisor.gas_backend.repository.JpaTest
import it.gasadvisor.gas_backend.repository.PrivilegeRepository
import it.gasadvisor.gas_backend.repository.RoleRepository
import it.gasadvisor.gas_backend.repository.UserRepository
import it.gasadvisor.gas_backend.util.JwtHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority


@SpringBootTest
internal class AuthenticationServiceTest @Autowired constructor(
    val service: AuthenticationService,
    val jwtHelper: JwtHelper
) {
    @Test
    fun `should create refresh token`() {
        val token = jwtHelper.createPrimaryJwt("diego", setOf(SimpleGrantedAuthority("authority")))
        val response = service.refreshToken(RefreshTokenRequest(token))
        val claimsAuthToken = jwtHelper.readJwt(response.authToken)
        val claimsRefreshToken = jwtHelper.readJwt(response.refreshToken)
        assertEquals("diego", claimsAuthToken.subject)
        assertEquals("diego", claimsRefreshToken.subject)
        assertTrue(claimsRefreshToken.expiration > claimsAuthToken.expiration)
        assertEquals("authority", (claimsAuthToken[JwtHelper.AUTHORITIES_CLAIM] as List<*>)[0])
        assertEquals("authority", (claimsRefreshToken[JwtHelper.AUTHORITIES_CLAIM] as List<*>)[0])
    }

}
