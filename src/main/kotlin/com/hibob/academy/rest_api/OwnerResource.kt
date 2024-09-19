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


@Controller
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    private fun validateOwner(owner: Owner) {
        val hasName = !owner.name.isNullOrBlank()
        val hasFirstNameAndLastName = !owner.firstName.isNullOrBlank() && !owner.lastName.isNullOrBlank()

        if (hasName && (owner.firstName != null || owner.lastName != null)) {
            throw BadRequestException("Owner cannot have both a name and first name/last name.")
        }

        if (!hasName && !hasFirstNameAndLastName) {
            throw BadRequestException("Owner must have either a full name or both first name and last name.")
        }
    }

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

    @POST
    @Path("/AddOwner")
    fun addOwner(newOwner: Owner): Response {
        validateOwner(newOwner)  // בדיקת תקינות של Owner
        owners.add(newOwner)
        return Response.status(Response.Status.CREATED).entity(newOwner).build()
    }


    //PUT: Update a owner by ownerId
    @PUT
    @Path("/{ownerId}")
    fun updateOwner(@PathParam("ownerId") ownerId: Long, updatedOwner: Owner): Response {
        validateOwner(updatedOwner)  // בדיקת תקינות של Owner
        val index = owners.indexOfFirst { it.ownerId == ownerId }
        return if (index >= 0) {
            owners[index] = updatedOwner.copy(ownerId = ownerId)
            Response.ok(updatedOwner).build()
        } else {
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