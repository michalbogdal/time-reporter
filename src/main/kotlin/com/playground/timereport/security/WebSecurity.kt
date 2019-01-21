package com.playground.timereport.security

import com.playground.timereport.domain.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@EnableWebSecurity
open class WebSecurity(
        private val userRepository: UserRepository,
        private val tokenService: TokenService
) : WebSecurityConfigurerAdapter(false) {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/app/**").authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager(), userRepository, tokenService))
                .addFilter(JWTAuthorizationFilter(authenticationManager(), userRepository, tokenService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val configuration = CorsConfiguration().applyPermitDefaultValues()

        // This allow us to expose the headers
        configuration.exposedHeaders = Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers", "Access-Token", "Uid")


        source.registerCorsConfiguration("/**", configuration)
        return source
    }

   /* @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return username -> userRepository.findByUsername(username).map(
            user -> User(user.username, user.password, ArrayList<GrantedAuthority>())
        )
        .orElseThrow( () -> UsernameNotFoundException(username))
    }*/
}