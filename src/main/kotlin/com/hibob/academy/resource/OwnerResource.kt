package com.hibob.academy.resource

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.service.CompanyRequest
import com.hibob.academy.service.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/owner/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
@Consumes(MediaType.APPLICATION_JSON)  // Specifies that this endpoint accepts JSON input
class OwnerResource @Autowired constructor(private val sql: DSLContext){
    private val dao = OwnerDao(sql)

    @POST
    @Path("getAllOwners")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllOwners(request: CompanyRequest): Response {
        // Check if companyId is null or invalid (for example, less than 1)
        if (request.companyId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid companyId provided").build()
        }

        // Call the DAO to fetch the owners based on the companyId
        val owners = dao.getAllOwners(request.companyId)

        // If no owners found, return a 404 response
        if (owners.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No owners found for the given company").build()
        }

        // Return the owners list as a JSON response
        return Response.ok(owners).build()
    }

    @POST
    @Path("insertNewOwner")
    fun insertNewOwner(owner: Owner): Response {
        // Insert the new owner (if it doesn't exist) using the DAO function
        val insertOwnerSerialId = dao.insertNewOwner(owner.ownerName, owner.employeeId, owner.companyId)

        // Check if the owner was inserted or already existed
        return if (insertOwnerSerialId > 0L) {
            Response.status(Response.Status.CREATED).entity("Owner successfully inserted").build()
        } else {
            Response.status(Response.Status.OK).entity("Owner already exists").build()
        }
    }
}