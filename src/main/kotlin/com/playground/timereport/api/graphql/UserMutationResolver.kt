package com.playground.timereport.api.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.playground.timereport.domain.service.UserService
import org.springframework.stereotype.Component

@Component
class UserMutationResolver(
        private val userService: UserService
) : GraphQLMutationResolver {

    fun logout(): String {
        return userService.invalidateToken()
    }

    fun changePassword(oldPassword: String, newPassword: String): String {
        return userService.changePassword(oldPassword, newPassword)
    }
}