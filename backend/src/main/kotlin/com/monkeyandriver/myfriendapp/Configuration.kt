package com.monkeyandriver.myfriendapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class Configuration @Autowired constructor(
){
    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}