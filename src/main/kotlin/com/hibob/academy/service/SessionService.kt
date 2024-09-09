package com.hibob.academy.service
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.Date
import javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA256

@Component
class SessionService {
    private val secretKey = "secret"

    fun createJWTToken(): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("email", "hibob@hibob.academy")
            .claim("username", "ronAzar")
            .claim("isAdmin", true)
            .setExpiration(Date(Date().time + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS512, HMAC_SHA256)
            .compact()
    }
}