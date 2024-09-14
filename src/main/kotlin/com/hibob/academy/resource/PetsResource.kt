package com.hibob.academy.resource

import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Controller

@Controller
@Path("/api/ron/azar/pets/")
@Produces(MediaType.APPLICATION_JSON)
class PetsResource