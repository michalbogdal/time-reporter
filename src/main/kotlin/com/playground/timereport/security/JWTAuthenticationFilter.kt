package com.playground.timereport.security

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.timereport.dao.UserDAO
import com.playground.timereport.security.SecurityConstants.HEADER_STRING
import com.playground.timereport.security.SecurityConstants.LOGIN_URL
import com.playground.timereport.security.SecurityConstants.TOKEN_PREFIX
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthenticationFilter(authenticationManager: AuthenticationManager?,
                              private val userDAO: UserDAO,
                              private val tokenService: TokenService
) : UsernamePasswordAuthenticationFilter() {

    init {
        this.authenticationManager = authenticationManager
        super.setFilterProcessesUrl(LOGIN_URL)
        super.setAuthenticationManager(authenticationManager)
    }


    @JsonAutoDetect
    data class LoginCredentials(@JsonProperty("username") val username: String, @JsonProperty("password") val password: String)

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication? {
        val loginCredentials = ObjectMapper().readValue(req.reader, LoginCredentials::class.java)
        return authenticationManager?.authenticate(UsernamePasswordAuthenticationToken(loginCredentials.username, loginCredentials.password))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {

        val username = (auth.principal as User).username
        val user = userDAO.findByUsername(username)

        val token = tokenService.createToken(username, user?.getSid().orEmpty())
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }
}