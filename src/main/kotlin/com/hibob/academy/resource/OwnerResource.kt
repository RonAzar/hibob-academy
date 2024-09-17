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
        val owners = ownerService.getAllOwners(companyId)
        return Response.ok(owners).build()
    }

    @POST
    @Path("insertNewOwner")
    fun insertNewOwner(newOwner: Owner): Response {
        ownerService.insertOwner(newOwner)
        return Response.status(Response.Status.CREATED).entity("Owner successfully inserted").build()
    }

    @GET
    @Path("getOwnerByPetId/{petId}/companyId/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getOwnerByPetId(
        @PathParam("petId") petId: Long,
        @PathParam("companyId") companyId: Long
    ): Response {
        val owner = ownerService.getOwnerByPetId(petId, companyId)
        return Response.ok(owner).build()
    }

    @GET
    @Path("getOwnerByEmployeeId/{employeeId}/CompanyId/{companyId}")
    fun getOwnerByEmployeeIdAndCompanyId(
        @PathParam("employeeId") employeeId: String,
        @PathParam("companyId") companyId: Long
    ): Response {
        val owner = ownerService.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId)
        return Response.ok(owner).build()
    }
}