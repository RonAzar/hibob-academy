package rest_api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller


@Controller
@Path("/api/ron/owners/envelopes")
@Produces(MediaType.APPLICATION_JSON)
class ownerResource {
    @GET
    @Path("/{ownerId}")
    fun getPetType(@PathParam("ownerId") ownerId: Long): Response {
        if (ownerId.toInt() == 123){
            throw NotFoundException("Pet not found")
        }
        else {
            Response.status(Response.Status.UNAUTHORIZED).build()
            return Response.ok("some Owner").build()
        }
    }
}