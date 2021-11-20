package com.monkeyandriver.myfriendapp.service

import com.monkeyandriver.myfriendapp.domain.User
import com.monkeyandriver.myfriendapp.dto.JwtTokenDTO
import com.monkeyandriver.myfriendapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

interface UserService {
    fun insertUser(username: String, password: String, firstName: String, lastName: String): User
    fun deleteUser(userId: String)
    fun insertFriend(userId: String, friendId: String)
    fun removeFriend(userId: String, friendId: String)
    fun getUser(userId: String): User
    fun getAllUsers(pageNumber: Int, query: String): List<User>
    fun getUserFriends(userId: String): List<User>
    fun verifyLogin(username: String, password: String): JwtTokenDTO
}

