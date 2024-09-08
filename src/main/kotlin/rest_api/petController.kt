package rest_api

import jakarta.ws.rs.GET
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/ron/pets/envelopes")
@Produces(MediaType.APPLICATION_JSON)

class petResource {
    @GET
    @Path("/{petId}/type")
    fun getPetType(@PathParam("petId") petId: Long): Response {
        if (petId.toInt() == 123){
            throw NotFoundException("Pet not found")
        }
        else {
            Response.status(Response.Status.UNAUTHORIZED).build()
            return Response.ok("parrot").build()
        }
    }
}