package com.playground.timereport.security

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

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication? {
        val username: String? = req.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY)
        val pass: String? = req.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY)
        return authenticationManager?.authenticate(UsernamePasswordAuthenticationToken(username, pass))
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