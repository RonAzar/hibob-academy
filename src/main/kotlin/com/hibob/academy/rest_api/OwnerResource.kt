package com.hibob.academy.rest_api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

val owners = mutableListOf(
    Owner(1L,"Ron", "Ron", "Azar", 101L, 10L),
    Owner(2L,"Or", null, null, 102L, 11L),
    Owner(3L,"Noam","Noam", null, 103L, 12L)
)

val oldOwners = mutableListOf(
    OldOwner(1L,"Ron Azar", 101L, 10L),
    OldOwner(2L,"Or", 102L, 11L),
    OldOwner(3L,"Bob", 103L, 12L)
)


@Controller
@Path("/api/ron/owners")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    //GET: Retrieve all owners
    @GET
    fun getAll() : Response {
        return Response.ok(owners).build()
    }

    //GET: Retrieve owner by ownerId
    @GET
    @Path("/{ownerId}")
    fun getById(@PathParam("ownerId") ownerId: Long): Response {
        val owner = owners.find { it.ownerId == ownerId }
        return when{
            owner == null ->{
                throw NotFoundException("No owner with id $ownerId")
            }
            ownerId == 123L ->{
                Response.status(Response.Status.UNAUTHORIZED).build()
            }
            else ->{
                Response.ok(owner.toString()).build()
            }
        }
    }

    @Path("/AddOwner")
    @POST
    fun addOwner(newOwner : Owner) : Response {

        owners.add(newOwner)
        return Response.status(Response.Status.CREATED).entity(newOwner).build()
    }

    @Path("/AddOwner")
    @POST
    fun addOwner(oldOwner: OldOwner) : Response {
        oldOwners.add(oldOwner)
        return Response.status(Response.Status.CREATED).entity(oldOwner).build()
    }

    //PUT: Update a owner by ownerId
    @PUT
    @Path("/{ownerId}")
    fun updateOwner(@PathParam("ownerId") ownerId: Long, updatedOwner: Owner): Response {
        val index = owners.indexOfFirst { it.ownerId == ownerId }
        return if (index>=0){
            owners[index] = updatedOwner.copy(ownerId = ownerId)
            Response.ok(updatedOwner).build()
        }
        else{
            throw NotFoundException("No owner with id $ownerId")
        }
    }

    //DELETE: Delete an owner by ownerId
    @DELETE
    @Path("/{ownerId}")
    fun deleteOwner(@PathParam("ownerId") ownerId: Long): Response {
        val owner = owners.find { it.ownerId == ownerId }
        return  owner?.let {
            owners.remove(owner)
            Response.ok("${owner.toString()} Has been removed").build()
        }?: throw NotFoundException("No owner with id $ownerId")
    }

}