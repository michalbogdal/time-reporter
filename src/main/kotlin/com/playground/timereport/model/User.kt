package com.playground.timereport.model

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

    @Column(name = "sid")
    private var sid: String

    fun getSid() = this.sid

    init {
        this.password = encodeWithMD5(password)
        this.sid = generateRandomSid()
    }


    private fun generateRandomSid(): String {
        return UUID.randomUUID().toString()
    }

    fun regenerateSid(){
        this.sid = generateRandomSid()
    }
}