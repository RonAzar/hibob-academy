package com.hibob.academy.rest_api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller


@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PetController(private val petService: PetServices) {

    @GET
    fun getAllPets(): Response {
        return Response.ok(petService.getAllPets()).build()
    }

    @GET
    @Path("/{petId}")
    fun getPetById(@PathParam("petId") petId: Long): Response {
        return Response.ok(petService.getPetById(petId)).build()
    }

    @POST
    fun addPet(newPet: PetServices.Pet): Response {
        return Response.ok(petService.addPet(newPet)).build()
    }

    @PUT
    @Path("/{petId}")
    fun updatePet(@PathParam("petId") petId: Long, updatedPet: PetServices.Pet): Response {
        return Response.ok(petService.updatePet(petId, updatedPet)).build()
    }

    @DELETE
    @Path("/{petId}")
    fun deletePet(@PathParam("petId") petId: Long): Response {
        return Response.ok("${petService.deletePet(petId).name} has been removed").build()
    }
}