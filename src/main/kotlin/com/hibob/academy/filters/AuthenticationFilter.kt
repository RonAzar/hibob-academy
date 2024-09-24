package com.hibob.academy.filters
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import org.springframework.stereotype.Component
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider


@Component
@Provider
class AuthenticationFilter : ContainerRequestFilter {

    companion object {
        private const val LOGIN_PATH = "api/auth/employee/login"
        const val COOKIE_NAME = "session_employee_cookie"
        const val SECRET_KEY = "L5t6JwZqHasdfghjkxcvbnfghjk1nGv3xTu9Rp8EyK7fcQ2zV1lPm7BxW5hQ2dNG2w8mK6E3jRQ1sVgYnU5cZ4t"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        if (requestContext.uriInfo.path == LOGIN_PATH) return

        val cookie = requestContext.cookies[COOKIE_NAME]?.value

        if (!verify(cookie)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build())
        }
    }

    private val jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()

    fun verify(cookie: String?): Boolean {
        return cookie?.let {
            try {
                jwtParser.parseClaimsJws(it)
                true
            } catch (ex: Exception) {
                false
            }
        } ?: false
    }
}