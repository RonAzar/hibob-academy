package com.hibob.academy.filters
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import org.springframework.stereotype.Component


@Component
class AuthenticationFilter : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext?) {
        if (requestContext.uriInfo.path == "To be implement") return

        //here we need to verify the JWT token
    }

}