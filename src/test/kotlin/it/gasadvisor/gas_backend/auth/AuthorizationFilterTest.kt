package it.gasadvisor.gas_backend.auth

import it.gasadvisor.gas_backend.config.AuthorizationFilter
import it.gasadvisor.gas_backend.util.JwtHelper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.TestPropertySource

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
class AuthorizationFilterTest @Autowired constructor(
    private val jwtHelper: JwtHelper
) {
    private val filter = AuthorizationFilter(jwtHelper)

    private val req = MockHttpServletRequest()
    private val res = MockHttpServletResponse()
    private val chain = MockFilterChain()

    @BeforeEach
    fun resetAuth() {
        SecurityContextHolder.getContext().authentication = null
    }

    @Test
    fun `should not set authentication`() {
        req.addHeader("authorization", "")
        filter.doFilter(req, res, chain)
        assertTrue(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should set authentication`() {
        req.addHeader("authorization", jwtHelper.createJwt("diego", mutableSetOf()))
        filter.doFilter(req, res, chain)
        assertTrue(SecurityContextHolder.getContext().authentication.name.equals("diego"))
    }


}
