package com.hibob.academy.resource

import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetType
import com.hibob.academy.service.OwnerService
import com.hibob.academy.service.PetService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/pets/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class PetsResource (private val petService: PetService){
//
//    @POST
//    @Path("getAllPetsByType")
//    fun getAllPetsByType(petType: PetType, companyId: Long): Response {
//        val petsList = petDao.getAllPetsByType(petType, companyId)
//        if (petsList.isEmpty())
//            return Response.noContent().build()
//        return Response.ok(petsList).build()
//    }
//
//    @POST
//    @Path("getAllPets")
//    fun getAllPets(companyId: Long): Response {
//        val petsList = petDao.getAllPets(companyId)
//        if (petsList.isEmpty())
//            return Response.noContent().build()
//        return Response.ok(petsList).build()
//    }
//
//    @POST
//    @Path("getPetById")
//    fun getPetById(petId: Long, companyId: Long): Response {
//        val pet = petDao.getPetById(petId, companyId)
//
//        pet?.let {
//            return Response.ok(pet).build()
//        }?: return Response.status(Response.Status.NOT_FOUND).build()
//    }
//
//    @POST
//    @Path("insertNewPet")
//    fun insertNewPet(newPet: Pet): Response {
//        val insertPetSerialId = petDao.insertNewPet(newPet.petName, newPet.petDateOfArrival, newPet.petType, newPet.companyId, newPet.ownerId)
//
//        if (insertPetSerialId < 0L){
//            return Response.status(Response.Status.OK).entity("Pet already exists").build()
//        }
//        return Response.status(Response.Status.CREATED).entity("Pet successfully inserted").build()
//    }
//
//    @PUT
//    @Path("updatePetOwnerId")
//    fun updatePetOwnerId(petId: Long, newOwnerId: Long, companyId: Long): Response{
//        val rowEffect = petDao.updatePetOwnerId(petId, newOwnerId, companyId)
//
//        if (rowEffect == 0){
//            return Response.status(Response.Status.BAD_REQUEST).entity("Owner id not updated! This pet may already has an owner id").build()
//        }
//        return Response.ok("Pet owner id Updated successfully").build()
//    }
}