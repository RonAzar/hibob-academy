package com.hibob.academy.filters
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import org.springframework.stereotype.Component
import com.hibob.academy.service.SessionService.Companion.SECRET_KEY
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider


@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {
    companion object {
        private const val LOGIN_PATH = "jwt/users/login"
        const val COOKIE_NAME = "ron_cookie_name"  // Replace with actual cookie name
    }

    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        // Verify the JWT token from the cookie
        val cookie = requestContext.cookies[COOKIE_NAME]?.value

        try {
            val jwtClaims = verify(cookie) ?: throw Exception("Invalid token")
        } catch (e: Exception) {
            // Catch any exceptions (including null or verification failure) and abort with UNAUTHORIZED status
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build())
        }
    }
    private val jwtParser = Jwts.parser().setSigningKey(SECRET_KEY)

    fun verify(cookie: String?): Jws<Claims>? =
        cookie?.let {
            try {
                jwtParser.parseClaimsJws(it)
            } catch (ex: Exception) {
                null
            }
        }
}