package com.playground.timereport.domain.repository

import com.playground.timereport.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {

    fun findByUsername(username: String): Optional<User>
}