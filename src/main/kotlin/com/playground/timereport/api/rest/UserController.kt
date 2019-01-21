package com.playground.timereport.api.rest

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.playground.timereport.domain.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("app/v1/")
class UserController(private val userService: UserService) {

    @PostMapping("/logout")
    fun invalidateToken(): String {
        return userService.invalidateToken()
    }

    @PostMapping("/changepassword")
    fun changePassword(@RequestBody changePassword: ChangePasswordDTO): String {
        return userService.changePassword(changePassword.oldPassword, changePassword.newPassword)
    }
}

@JsonAutoDetect
data class ChangePasswordDTO(@JsonProperty("oldPassword") val oldPassword: String, @JsonProperty("newPassword") val newPassword: String)