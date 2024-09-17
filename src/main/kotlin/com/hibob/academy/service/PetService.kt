package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PetService @Autowired constructor(private val petDao: PetDao) {
    fun getAllPets(companyId: Long): List<PetData> {
        return petDao.getAllPets(companyId)
    }

    fun insertNewPet(newPet: Pet): Long {
        // Insert the new pet and return the generated ID or -1 if the operation failed
         val petId = petDao.insertNewPet(newPet)
         if(petId < 0L){
            throw IllegalArgumentException("Insertion failed...")
        }
        return petId
    }

    fun getPetById(petId: Long, companyId: Long): PetData {
        val pet = petDao.getPetById(petId, companyId) ?: throw NoSuchElementException("Pet not found for the given ID")
        return pet
    }

    fun getAllPetsByType(type: PetType, companyId: Long): List<PetData> {
        return petDao.getAllPetsByType(type, companyId)
    }

    fun updatePetOwnerId(petId: Long, petOwnerId: Long, companyId: Long): String {
        // Retrieve the pet by ID and companyId
        val pet = petDao.getPetById(petId, companyId)
            ?: throw NoSuchElementException("Pet not found for the given ID")

        // Ensure the pet doesn't already have an owner
        if (pet.ownerId != null) {
            throw IllegalArgumentException("Pet already has an owner")
        }

        // Update the ownerId if it is currently null
        val rowsAffected = petDao.updatePetOwnerId(petId, petOwnerId, companyId)
        if (rowsAffected == 0) {
            throw IllegalArgumentException("Failed to update pet's owner. The pet may already have an owner.")
        }
        return "Pet owner ID updated successfully"
    }
}