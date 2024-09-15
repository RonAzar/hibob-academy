package com.hibob.academy.resource

import com.hibob.academy.dao.Owner
import com.hibob.academy.service.OwnerService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/owner/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class OwnerResource (private val ownerService: OwnerService){

    @GET
    @Path("getAllOwners/{companyId}")
    fun getAllOwners(@PathParam("companyId") companyId: Long): Response {
        if (companyId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid companyId provided").build()
        }

        val owners = ownerService.getAllOwners(companyId)

        if (owners.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No owners found for the given company").build()
        }

        return Response.ok(owners).build()
    }


    @POST
    @Path("insertNewOwner")
    fun insertNewOwner(newOwner: Owner): Response {
        return try {
            val insertOwnerSerialId = ownerService.insertOwner(newOwner)
            if (insertOwnerSerialId > 0L) {
                Response.status(Response.Status.CREATED).entity("Owner successfully inserted").build()
            } else {
                Response.status(Response.Status.OK).entity("Owner already exists").build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: ${e.message}").build()
        }
    }

    @GET
    @Path("getOwnerByPetId/{petId}/companyId/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getOwnerByPetId(
        @PathParam("petId") petId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        val owner = ownerService.getOwnerByPetId(petId, companyId)

        return if (owner != null) {
            Response.ok(owner).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
        }
    }

    @GET
    @Path("getOwnerByEmployeeId/{employeeId}/CompanyId/{companyId}")
    fun getOwnerByEmployeeIdAndCompanyId(
        @PathParam("employeeId") employeeId: String,
        @PathParam("companyId") companyId: Long
    ): Response {
        val owner = ownerService.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId)

        return if (owner != null) {
            Response.ok(owner).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).entity("Owner not found").build()
        }
    }
}