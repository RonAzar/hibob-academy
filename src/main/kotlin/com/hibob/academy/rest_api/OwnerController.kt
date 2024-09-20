package com.hibob.academy.rest_api

import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class OwnerController(val ownerService: OwnerServices, val originOwnerService: OwnerService) {

    @GET
    fun getAll(): Response {
        val owners = ownerService.getAllOwners()
        return Response.ok(owners).build()
    }

    @GET
    @Path("/{ownerId}")
    fun getById(@PathParam("ownerId") ownerId: Long): Response {
        return Response.ok(ownerService.getOwnerById(ownerId)).build()
    }

    @POST
    fun addOwner(newOwner: OwnerServices.Owner): Response {
        return Response.ok(ownerService.addOwner(newOwner)).build()
    }

    @PUT
    @Path("/{ownerId}")
    fun updateOwner(@PathParam("ownerId") ownerId: Long, updatedOwner: OwnerServices.Owner): Response {
        return Response.ok(ownerService.updateOwner(ownerId, updatedOwner)).build()
    }

    @DELETE
    @Path("/{ownerId}")
    fun deleteOwner(@PathParam("ownerId") ownerId: Long): Response {
        return Response.ok("${ownerService.deleteOwner(ownerId)} has been removed").build()
    }
}