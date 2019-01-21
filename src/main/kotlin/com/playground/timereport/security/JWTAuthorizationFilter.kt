package com.playground.timereport.security

import com.playground.timereport.domain.repository.UserRepository
import com.playground.timereport.security.SecurityConstants.HEADER_STRING
import com.playground.timereport.security.SecurityConstants.TOKEN_PREFIX
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter(authenticationManager: AuthenticationManager?,
                             private val userRepository: UserRepository,
                             private val tokenService: TokenService
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader(HEADER_STRING)

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
        return

    }

    private fun getAuthentication(request: HttpServletRequest): AbstractAuthenticationToken? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {

            val tokenClaims = tokenService.getTokenClaims(token.removePrefix(TOKEN_PREFIX))
            if(tokenClaims != null) {

                val username = tokenClaims.body[TokenService.USERNAME].toString()
                val salt = tokenClaims.body[TokenService.SALT].toString()
                val user = userRepository.findByUsername(username)

                if (salt == user.map { user -> user.getSalt() }.orElse("")) {
                    return UsernamePasswordAuthenticationToken(username, null, ArrayList<GrantedAuthority>())
                }
            }
        }
        return null
    }

}