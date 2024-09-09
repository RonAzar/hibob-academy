package com.hibob.academy.resource

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import com.hibob.academy.service.SessionService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller

data class User(val email: String,val name: String, val isAdmin: Boolean)

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/users")
class AuthenticationResourse(private val service: SessionService) {
    @Path("/Login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addNewUser(newUser: User): Response {
        val session = SessionService()
        val afterJwt = session.createJWTToken(newUser)  // Assuming createJWTToken returns a JWT
        return Response.status(Response.Status.CREATED).entity(afterJwt).build()    }
}