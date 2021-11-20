package com.monkeyandriver.myfriendapp.dto

import com.monkeyandriver.myfriendapp.domain.User

data class UserDTO(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String
) {
    constructor(user: User) : this(user.id, user.username, user.firstName, user.lastName)
}
