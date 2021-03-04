package it.gasadvisor.gas_backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import it.gasadvisor.gas_backend.util.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

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

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/test").permitAll()
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
            .and().exceptionHandling().authenticationEntryPoint(AuthenticationEntrypointConfig())
            .and().cors()
            .and().csrf().disable()
            .addFilter(AuthenticationFilter(authenticationManager(), mapper, jwtHelper))
            .addFilterAfter(AuthorizationFilter(jwtHelper), AuthenticationFilter::class.java).anonymous().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
            "/public/**"
        )
    }
}
