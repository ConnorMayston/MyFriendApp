package com.monkeyandriver.myfriendapp.service

import com.monkeyandriver.myfriendapp.exception.InvalidJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtServiceImpl : JwtService{
    @Value("\${jwt.secret}")
    private var jwtSecret = ""
    override fun generateToken(sub: String): String {
        val key = SecretKeySpec(jwtSecret.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts.builder().setSubject(sub).setIssuedAt(Date()).signWith(key).compact()
    }

    override fun parseToken(jwt: String): String {
        val key = SecretKeySpec(jwtSecret.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).body.subject
        }catch (exception: SignatureException){
            throw InvalidJwtException()
        }
    }


}