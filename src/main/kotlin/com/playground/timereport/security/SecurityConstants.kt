package com.playground.timereport.security

object SecurityConstants {
    const val APP_NAME = "TimeReport"
    const val JWT_SECRET_PROPERTY = "authentication.jwt.secret"
    const val EXPIRATION_TIME: Long = 864000000 // 10 day
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
    const val LOGIN_URL = "/app/login"
}