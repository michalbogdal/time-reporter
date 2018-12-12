package com.playground.timereport.dao

import com.playground.timereport.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDAO : JpaRepository<User, String> {

    fun findByUsername(username: String): User?
}