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
@Path("/api/pets/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class PetsResource(private val petService: PetService) {
    @GET
    @Path("companies/{companyId}/pets/type/{petType}")
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
    @Path("companies/{companyId}/pets/{petId}")
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
    @Path("pets/{petId}/owner/{newOwnerId}/company/{companyId}")
    fun updatePetOwnerId(
        @PathParam("petId") petId: Long,
        @PathParam("newOwnerId") newOwnerId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        petService.updatePetOwnerId(petId, newOwnerId, companyId)
        return Response.ok().build()
    }

    @GET
    @Path("owners/{ownerId}/company/{companyId}/pets")
    fun getPetsByOwnerId(
        @PathParam("ownerId") ownerId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        val pets = petService.getPetsByOwnerId(ownerId, companyId)
        return Response.ok(pets).build()
    }

    // New method for petTypesAmount
    @GET
    @Path("types/company/{companyId}")
    fun petTypesAmount(@PathParam("companyId") companyId: Long): Response {
        val petTypeCountMap = petService.getPetTypesAmount(companyId)
        return Response.ok(petTypeCountMap).build()
    }

    @POST
    @Path("/owner/{ownerId}/company/{companyId}/pets/adopt")
    fun adoptPets(
        @PathParam("ownerId") ownerId: Long,
        @PathParam("companyId") companyId: Long,
        petIds: List<Long>
    ): Response {
        val petsAdoptedCount = petService.adoptMultiplePets(ownerId, petIds, companyId)
        return Response.ok("$petsAdoptedCount pets were successfully adopted.").build()
    }

    @POST
    @Path("/owner/{ownerId}/company/{companyId}/pets/create")
    fun createMultiplePets(
        @PathParam("ownerId") ownerId: Long,
        @PathParam("companyId") companyId: Long,
        petRecords: List<PetRecord>
    ): Response {
        petService.createMultiplePetsUsingBatch(ownerId, petRecords, companyId)
        return Response.ok("Pets have been successfully created!").build()
    }

}