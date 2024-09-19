package com.hibob.academy.rest_api

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import java.util.Date

val pets = mutableListOf(
    Pet(1L, "Buddy", type = "Dog", companyId = 101, firstName = null, lastName = null, dateOfArrival = Date()),
    Pet(2L, "Whiskers", type = "Cat", companyId = 102,firstName = null, lastName = null, dateOfArrival = Date()),
    Pet(3L, "Polly", type = "Parrot", companyId = 103,firstName = null, lastName = null, dateOfArrival =Date())
)

@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)

class PetResource {

    private fun validatePet(pet: Pet) {
        val hasName = !pet.name.isNullOrBlank()
        val hasFirstNameAndLastName = !pet.firstName.isNullOrBlank() && !pet.lastName.isNullOrBlank()

        if (hasName && (pet.firstName != null || pet.lastName != null)) {
            throw BadRequestException("Pet cannot have both a name and first name/last name.")
        }

        if (!hasName && !hasFirstNameAndLastName) {
            throw BadRequestException("Pet must have either a full name or both first name and last name.")
        }
    }
    // GET: Retrieve pet by ID
    //Example-> "http://localhost:8080/api/ron/pets/envelopes/1"
    @GET
    @Path("/{petId}/type")
    fun getPetType(@PathParam("petId") petId: Long): Response {
        val pet = pets.find { it.petId == petId }
        return when {
            pet == null -> {
                throw NotFoundException("Pet not found")  // Pet not found
            }
            petId == 123L -> {
                Response.status(Response.Status.UNAUTHORIZED).build()  // Unauthorized for petId 123
            }
            else -> {
                Response.ok().entity(pet).build()
            }
        }
    }

    // GET: Retrieve all pets
    //Example-> "http://localhost:8080/api/ron/pets/envelopes"
    @GET
    fun getAllPets(): Response {
        return Response.ok(pets).build()
    }

    // POST: Add a new pet
    //Example-> "http://localhost:8080/api/ron/pets/envelopes"
    //     "Content-Type: application/json" \
    //     '{
    //          "name": "Max",
    //          "type": "Dog",
    //          "companyId": 104,
    //          "dateOfArrival": "2024-09-06T12:00:00Z"
    //     }'
    @POST
    fun addPet(newPet: Pet): Response {
        validatePet(newPet)
        pets.add(newPet)
        return Response.status(Response.Status.CREATED).entity(newPet).build()
    }


    // PUT: Update a pet by ID
    //Example-> "http://localhost:8080/api/ron/pets/envelopes/1" \
    //     "Content-Type: application/json"
    //     {
    //          "name": "Buddy Updated",
    //          "type": "Dog",
    //          "companyId": 101,
    //          "dateOfArrival": "2024-09-06T12:00:00Z"
    //     }
    @PUT
    @Path("/{petId}")
    fun updatePet(@PathParam("petId") petId: Long, updatedPet: Pet): Response {
        validatePet(updatedPet)
        val index = pets.indexOfFirst { it.petId == petId }
        return if (index >= 0) {
            pets[index] = updatedPet.copy(petId = petId)
            Response.ok(updatedPet).build()
        } else {
            throw NotFoundException("Pet not found")
        }
    }

    // DELETE: Delete a pet by ID
    //Example-> "http://localhost:8080/api/ron/pets/envelopes/1"
    @DELETE
    @Path("/{petId}")
    fun deletePet(@PathParam("petId") petId: Long): Response {
        val pet = pets.find { it.petId == petId }
        return if (pet != null) {
            pets.remove(pet)
            Response.ok("Pet with ID $petId deleted").build()
        } else {
            throw NotFoundException("Pet not found")
        }
    }
}