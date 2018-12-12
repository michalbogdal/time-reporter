package com.playground.timereport.security

import com.playground.timereport.dao.UserDAO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailServiceImpl(private val userDAO: UserDAO) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        if (username != null && !username.isBlank()) {
            val user = userDAO.findByUsername(username)
            if (user != null) {
                return User(username, user.password, ArrayList<GrantedAuthority>())
            }
        }
        throw UsernameNotFoundException(username);
    }
}
