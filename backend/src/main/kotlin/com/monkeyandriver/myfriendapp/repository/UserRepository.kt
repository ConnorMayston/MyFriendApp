package com.monkeyandriver.myfriendapp.repository

import com.monkeyandriver.myfriendapp.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable

interface UserRepository : JpaRepository<User, String>{
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User
    fun findAllByUsername(username: String, pageable: Pageable): List<User>
}