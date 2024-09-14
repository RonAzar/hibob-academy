package com.hibob.academy.resource

import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller

@Controller  // Marks the class as a controller, meaning it's responsible for handling incoming HTTP requests
@Path("/api/ron/azar/owner/")  // Defines the base URL path that this controller will handle
@Produces(MediaType.APPLICATION_JSON)  // Specifies that the responses produced by this controller will be in JSON format
class OwnerResource
