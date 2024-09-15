package com.hibob.academy.resource

import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/owner/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class OwnerResource @Autowired constructor(private val ownerService: OwnerService){

//    @POST
//    @Path("getAllOwners")
//    fun getAllOwners(companyId: Long): Response {
//        // Check if companyId is null or invalid (for example, less than 1)
//        if (companyId <= 0) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid companyId provided").build()
//        }
//
//        // Call the DAO to fetch the owners based on the companyId
//        val owners = ownerDao.getAllOwners(request.companyId)
//
//        // If no owners found, return a 404 response
//        if (owners.isEmpty()) {
//            return Response.status(Response.Status.NOT_FOUND).entity("No owners found for the given company").build()
//        }
//
//        // Return the owners list as a JSON response
//        return Response.ok(owners).build()
//    }

    @POST
    @Path("insertNewOwner")
    fun insertNewOwner(@RequestBody owner: OwnerService.Owner): Response {
        // Insert the new owner (if it doesn't exist) using the DAO function
        val insertOwnerSerialId = ownerService.insertOwner(owner.ownerName, owner.employeeId, owner.companyId)

        // Check if the owner was inserted or already existed
        return if (insertOwnerSerialId > 0L) {
            Response.status(Response.Status.CREATED).entity("Owner successfully inserted").build()
        } else {
            Response.status(Response.Status.OK).entity("Owner already exists").build()
        }
    }

//    @POST
//    @Path("getOwnerByPetId")
//    fun getOwnerByPetId(petId: Long, companyId: Long): Response {
//        val owner = ownerDao.getOwnerByPetId(petId,companyId)
//
//        if (owner != null)
//            return Response.ok(owner).build()
//
//        return Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
//    }
//
//    @POST
//    @Path("getOwnerByEmployeeIdAndCompanyId")
//    fun getOwnerByEmployeeIdAndCompanyId(employeeId: String, companyId: Long): Response {
//        val owner = ownerDao.getOwnerByEmployeeIdAndCompanyId(employeeId,companyId)
//
//        if (owner != null)
//            return Response.ok(owner).build()
//
//        return Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
//    }
}