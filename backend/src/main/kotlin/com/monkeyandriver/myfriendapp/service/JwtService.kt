package com.monkeyandriver.myfriendapp.service

interface JwtService {
    fun generateToken(sub: String): String
    fun parseToken(jwt: String): String
}