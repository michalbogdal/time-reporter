package com.playground.timereport.domain.model

import com.google.common.hash.Hashing
import java.nio.charset.Charset
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "app_user")
data class User(
        @Id
        @Column(name = "username")
        val username: String,

        @Column(name = "password")
        var password: String,

        @Column(name = "email")
        val email: String
) {

    companion object {
        fun encodeWithMD5(password: String): String {
            val hasher = Hashing.md5().newHasher()
            hasher.putString(password, Charset.defaultCharset())
            return "{MD5}" + hasher.hash().toString()
        }
    }

    @Column(name = "salt")
    private var salt: String

    fun getSalt() = this.salt

    init {
        this.password = encodeWithMD5(password)
        this.salt = generateSalt()
    }

    private fun generateSalt(): String {
        return UUID.randomUUID().toString()
    }

    fun regenerateSalt(){
        this.salt = generateSalt()
    }
}