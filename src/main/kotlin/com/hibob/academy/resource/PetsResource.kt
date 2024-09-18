package com.hibob.academy.resource

import com.hibob.academy.dao.PetRecord
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
    @Path("/companies/{companyId}/pets/type/{petType}")
    fun getAllPetsByType(@PathParam("petType") petType: PetType, @PathParam("companyId") companyId: Long): Response {
        val pet = petService.getAllPetsByType(petType, companyId)
        return Response.ok(pet).build()
    }

    @GET
    @Path("getAllPets/{companyId}")
    fun getAllPets(@PathParam("companyId") companyId: Long): Response {
        val petsList = petService.getAllPets(companyId)
        return Response.ok(petsList).build()
    }

    @GET
    @Path("/companies/{companyId}/pets/{petId}")
    fun getPetById(@PathParam("petId") petId: Long, @PathParam("companyId") companyId: Long): Response {
        val pet = petService.getPetById(petId, companyId)
        return Response.ok(pet).build()
    }

    @POST
    @Path("insertNewPet")
    fun insertNewPet(newPet: PetRecord): Response {
        petService.insertNewPet(newPet)
        return Response.ok(newPet).build()
    }

    @PUT
    @Path("/pets/{petId}/owner/{newOwnerId}/company/{companyId}")
    fun updatePetOwnerId(
        @PathParam("petId") petId: Long,
        @PathParam("newOwnerId") newOwnerId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        val resultMessage = petService.updatePetOwnerId(petId, newOwnerId, companyId)
        return Response.ok(resultMessage).build()
    }
}