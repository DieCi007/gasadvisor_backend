package it.gasadvisor.gas_backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import it.gasadvisor.gas_backend.util.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig @Autowired constructor(
    private val mapper: ObjectMapper,
    private val jwtHelper: JwtHelper
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    @Bean
    fun getAuthManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(http: HttpSecurity) {
        val authenticationFilter = AuthenticationFilter(getAuthManager(), mapper, jwtHelper)
        authenticationFilter.setFilterProcessesUrl("/api/v1/login")
        http.authorizeRequests()
            .anyRequest().permitAll()
            .and().exceptionHandling().authenticationEntryPoint(AuthenticationEntrypointConfig())
            .and().cors()
            .and().csrf().disable()
            .addFilter(authenticationFilter)
            .addFilterAfter(AuthorizationFilter(jwtHelper), AuthenticationFilter::class.java).anonymous().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun corsConfigurationSource(
        @Value("\${it.gasadvisor.allowedOrigins:*}") allowedOrigins: Array<String>
    ): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(*allowedOrigins)
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/",
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
        )
    }
}
