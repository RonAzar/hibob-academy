package com.hibob.academy.resource

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.PetService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/pets/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class PetsResource (private val petService: PetService){
    @GET
    @Path("getAllPetsByType/{petType}/companyId/{companyId}")
    fun getAllPetsByType(@PathParam("petType") petType: PetType, @PathParam("companyId") companyId: Long): Response {
        return try {
            val pet = petService.getAllPetsByType(petType, companyId)
            Response.ok(pet).build()
        } catch (e: IllegalArgumentException) {
            Response.status(Response.Status.NOT_FOUND).entity(e.message).build()
        }
    }

    @GET
    @Path("getAllPets/{companyId}")
    fun getAllPets(@PathParam("companyId") companyId: Long): Response {
        val petsList = petService.getAllPets(companyId)
        return if (petsList.isEmpty())
            Response.noContent().build()
        else
            Response.ok(petsList).build()
    }

    @GET
    @Path("getPetById/{petId}/companyId/{companyId}")
    fun getPetById(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
        return try {
            val pet = petService.getPetById(petId, companyId)
            Response.ok(pet).build()
        } catch (e: IllegalArgumentException) {
            Response.status(Response.Status.NOT_FOUND).entity(e.message).build()
        }
    }

    @POST
    @Path("insertNewPet")
    fun insertNewPet(newPet: Pet): Response {
        val insertPetSerialId = petService.insertNewPet(newPet)
        return if (insertPetSerialId < 0L) {
            Response.status(Response.Status.OK).entity("Pet already exists").build()
        } else {
            Response.status(Response.Status.CREATED).entity("Pet successfully inserted").build()
        }
    }

    @PUT
    @Path("updatePetOwnerId/{petId}/newOwnerId/{newOwnerId}/companyId/{companyId}")
    fun updatePetOwnerId(
        @PathParam("petId") petId: Long,
        @PathParam("newOwnerId") newOwnerId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        return try {
            val resultMessage = petService.updatePetOwnerId(petId, newOwnerId, companyId)
            Response.ok(resultMessage).build()
        } catch (e: IllegalArgumentException) {
            Response.status(Response.Status.BAD_REQUEST).entity(e.message).build()
        }
    }
}