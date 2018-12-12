package com.playground.timereport.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.playground.timereport.dao.TimeReportDao
import com.playground.timereport.dao.UserDAO
import com.playground.timereport.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserMutationResolver(
        private val userDAO: UserDAO
) : GraphQLMutationResolver {

    fun logout(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.principal.toString()
        val user = userDAO.findByUsername(username)
        if (user != null) {
            user.regenerateSid()
            userDAO.save(user)
            return "success"
        }

        return "failed"
    }

    fun changePassword(oldPassword: String, newPassword: String): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.principal.toString()
        val user = userDAO.findByUsername(username)
        if (user != null) {

            if (User.encodeWithMD5(oldPassword) == user.password) {
                user.password = User.encodeWithMD5(newPassword)
                userDAO.save(user)
                return "password changed"
            }
        }

        return "couldn't changed"
    }
}