package com.playground.timereport.security

import com.playground.timereport.domain.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        if (username != null && !username.isBlank()) {
            val user = userRepository.findByUsername(username)
            if (user.isPresent) {
                return User(username, user.get().password, ArrayList<GrantedAuthority>())
            }
        }
        throw UsernameNotFoundException(username);
    }
}
