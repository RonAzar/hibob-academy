package com.hibob.academy.filters
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import org.springframework.stereotype.Component
import com.hibob.academy.service.SessionService.Companion.secretKey
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider


@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == "jwt/users/login") return

        // Verify the JWT token from the cookie
        val cookie = requestContext.cookies
        val jwtClaims = verify(cookie["ron_cookie_name"]?.value)

        if (jwtClaims == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build())
        }
    }
    private val jwtParser = Jwts.parser().setSigningKey(secretKey)

    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try {
                jwtParser.parseClaimsJws(it)
            } catch (ex: Exception) {
                null
            }
        }
}