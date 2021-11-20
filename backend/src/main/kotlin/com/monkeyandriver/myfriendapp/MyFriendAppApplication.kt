package com.monkeyandriver.myfriendapp

import com.monkeyandriver.myfriendapp.domain.User
import com.monkeyandriver.myfriendapp.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyFriendAppApplication @Autowired constructor(repository: UserRepository){
    init {
        for (i in 1..43) {
            repository.save(User(getRandomString(5), getRandomString(5), getRandomString(5), getRandomString(5)))
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MyFriendAppApplication>(*args)
}

fun getRandomString(length: Int): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}
