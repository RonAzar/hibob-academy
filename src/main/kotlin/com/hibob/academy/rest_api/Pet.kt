package com.hibob.academy.rest_api

import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service
import java.util.Date

@Service
class PetServices {
    data class Pet(
        val petId: Long,
        val name: String?,
        val firstName: String?,
        val lastName: String?,
        val type: String,
        val companyId: Int,
        val dateOfArrival: Date
    )

    private val pets = mutableListOf(
        Pet(1L, "Buddy", type = "Dog", companyId = 101, firstName = null, lastName = null, dateOfArrival = Date()),
        Pet(2L, "Whiskers", type = "Cat", companyId = 102, firstName = null, lastName = null, dateOfArrival = Date()),
        Pet(3L, "Polly", type = "Parrot", companyId = 103, firstName = null, lastName = null, dateOfArrival = Date())
    )

    fun validatePet(pet: Pet) {
        ValidationUtils.validateNameFields(pet.name, pet.firstName, pet.lastName)
    }

    fun getAllPets(): List<Pet> {
        return pets
    }

    fun getPetById(petId: Long): Pet {
        return pets.find { it.petId == petId }
            ?: throw NotFoundException("Pet not found")
    }

    fun addPet(newPet: Pet): Pet {
        validatePet(newPet)
        pets.add(newPet)
        return newPet
    }

    fun updatePet(petId: Long, updatedPet: Pet): Pet {
        validatePet(updatedPet)
        val index = pets.indexOfFirst { it.petId == petId }
        if (index >= 0) {
            pets[index] = updatedPet.copy(petId = petId)
            return updatedPet
        } else {
            throw NotFoundException("Pet not found")
        }
    }

    fun deletePet(petId: Long): Pet {
        val pet = pets.find { it.petId == petId }
            ?: throw NotFoundException("Pet not found")
        pets.remove(pet)
        return pet
    }
}