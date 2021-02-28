package it.gasadvisor.gas_backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import it.gasadvisor.gas_backend.util.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
    private val mapper: ObjectMapper,
    private val jwtHelper: JwtHelper
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .and().addFilter(AuthenticationFilter(authenticationManager(), mapper, jwtHelper))
            .addFilterAfter(AuthorizationFilter(jwtHelper), AuthenticationFilter::class.java).anonymous().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests().antMatchers("/api/**").authenticated()
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
            "/webjars/**",
            "/test",
            "/public/**"
        )
    }
}
