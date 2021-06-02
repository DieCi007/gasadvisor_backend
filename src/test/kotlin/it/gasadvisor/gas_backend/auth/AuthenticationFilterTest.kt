package it.gasadvisor.gas_backend.auth

import com.fasterxml.jackson.databind.ObjectMapper
import it.gasadvisor.gas_backend.api.auth.contract.AuthenticationRequest
import it.gasadvisor.gas_backend.config.AuthenticationFilter
import it.gasadvisor.gas_backend.util.JwtHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class AuthenticationFilterTest @Autowired constructor(
    jwtHelper: JwtHelper,
    private val mapper: ObjectMapper,
    authenticationManager: AuthenticationManager
) {
    private val filter = AuthenticationFilter(authenticationManager, mapper, jwtHelper)

    private lateinit var req: MockHttpServletRequest
    private lateinit var res: MockHttpServletResponse
    private lateinit var chain: MockFilterChain

    @BeforeEach
    fun resetRequest() {
        req = MockHttpServletRequest()
        res = MockHttpServletResponse()
        chain = MockFilterChain()
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    fun `should not work for wrong formed request`() {
        req.setContent("".toByteArray())
        assertThrows<AuthenticationException> { filter.attemptAuthentication(req, res) }
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    fun `should not work for wrong user`() {
        req.setContent(mapper.writeValueAsBytes(AuthenticationRequest(username = "admin", password = "")))
        assertThrows<AuthenticationException> { filter.attemptAuthentication(req, res) }
    }


}
