package com.hibob.academy.service
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.Date
import io.jsonwebtoken.security.Keys
import java.security.Key
import com.hibob.academy.resource.User

@Component
class SessionService {
    private val secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    fun createJWTToken(user: User): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("email", user.email)
            .claim("username", user.name)
            .claim("isAdmin", user.isAdmin)
            .setExpiration(Date(Date().time + 24 * 60 * 60 * 1000))
            .signWith(secretKey)
            .compact()
    }
}