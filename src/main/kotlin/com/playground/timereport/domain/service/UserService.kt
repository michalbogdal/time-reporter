package com.playground.timereport.domain.service

import com.playground.timereport.domain.repository.UserRepository
import com.playground.timereport.domain.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserService(private val userRepository: UserRepository) {

    fun getLoggedUser(): Optional<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.principal.toString()
        return userRepository.findByUsername(username)
    }

    fun invalidateToken(): String {
        return getLoggedUser().map { user ->
            user.regenerateSalt()
            userRepository.save(user)
            "success"
        }.orElse("failed")
    }

    fun changePassword(oldPassword: String, newPassword: String): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.principal.toString()
        val user = userRepository.findByUsername(username)
        if (user.isPresent) {

            if (User.encodeWithMD5(oldPassword) == user.get().password) {
                user.get().password = User.encodeWithMD5(newPassword)
                userRepository.save(user.get())
                return "password changed"
            }
        }

        return "couldn't changed"
    }
}