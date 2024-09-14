package com.hibob.academy.resource

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.OwnerData
import com.hibob.academy.dao.OwnerTable
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/owner/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
class OwnerResource @Autowired constructor(private val sql: DSLContext){
    val owner = OwnerTable.instance
    private val dao = OwnerDao(sql)

    @POST
    @Path("insertNewOwner")
    fun insertNewOwner(ownerData: OwnerData): Response {
        // Insert the new owner (if it doesn't exist) using the DAO function
        val insertOwnerSerialId = dao.insertNewOwner(ownerData.ownerName, ownerData.employeeId, ownerData.companyId)

        // Check if the owner was inserted or already existed
        return if (insertOwnerSerialId > 0L) {
            Response.status(Response.Status.CREATED).entity("Owner successfully inserted").build()
        } else {
            Response.status(Response.Status.OK).entity("Owner already exists").build()
        }
    }
}
