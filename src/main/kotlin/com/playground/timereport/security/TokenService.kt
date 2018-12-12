package com.playground.timereport.security


import com.playground.timereport.security.SecurityConstants.APP_NAME
import com.playground.timereport.security.SecurityConstants.EXPIRATION_TIME
import com.playground.timereport.security.SecurityConstants.JWT_SECRET_PROPERTY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
        @Autowired val env: Environment
) {

    companion object {
        val LOGGER = LoggerFactory.getLogger(TokenService::class.java)!!

        const val APP = "app"
        const val USERNAME = "username"
        const val SID = "sid"
    }

    fun createToken(username: String, sid: String): String {

        val claims: HashMap<String, Any?> = HashMap()
        claims.put(APP, APP_NAME)
        claims.put(USERNAME, username)
        claims.put(SID, sid)

        val appSecret = env.getProperty(JWT_SECRET_PROPERTY)

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, appSecret)
                .compact()

    }

    fun getTokenClaims(token: String): Jws<Claims>? {
        val appSecret = env.getProperty(JWT_SECRET_PROPERTY)

        try {
            return Jwts.parser().setSigningKey(appSecret).parseClaimsJws(token)

        } catch (e: Exception) {
            LOGGER.warn("problem with token {}, {}", token, e.message, e)
        }
        return null
    }
}