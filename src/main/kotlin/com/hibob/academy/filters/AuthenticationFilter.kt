package com.hibob.academy.filters
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import org.springframework.stereotype.Component
import com.hibob.academy.service.SessionService


@Component
class AuthenticationFilter : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == "To be implement") return

        //here we need to verify the JWT token

    }
    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try {
                Jwts.parser().setSigningKey(SessionService.secretKey).parseClaimsJws(it)
            } catch (ex: Exception) {
                null
            }
        }
}