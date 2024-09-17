package com.hibob.academy.service

import com.hibob.academy.dao.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PetService @Autowired constructor(
    private val petDao: PetDao,
    private val ownerDao: OwnerDao,
) {
    fun getAllPets(companyId: Long): List<PetData> {
        return petDao.getAllPets(companyId)
    }

    fun insertNewPet(newPet: PetRecord): Long {
        // Insert the new pet and return the generated ID or -1 if the operation failed
        val petId = petDao.insertNewPet(newPet)
        if(petId < 0L){
            throw IllegalArgumentException("Pet insertion failed...")
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

    fun updatePetOwnerId(petId: Long, petOwnerId: Long, companyId: Long) {
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
    }

    fun  getPetsByOwnerId(ownerId: Long, companyId: Long): List<PetData> {
        if (ownerId < 0) {
            throw IllegalArgumentException("Owner does not exist invalid owner id!")
        }
        if (ownerDao.getAllOwners(companyId).none { it.ownerId == ownerId }) {
            throw NoSuchElementException("Owner does not exist!")
        }
        val pets = petDao.getPetsByOwnerId(ownerId, companyId)
        if (pets.isEmpty()) {
            throw NoSuchElementException("This owner does not has any pets.")
        }
        return pets
    }

    fun petTypesAmount(companyId: Long): Map<PetType, Long> {
        val petsMap = petDao.petTypesAmount(companyId)

        if (petsMap.isEmpty()) {
            throw NoSuchElementException("This company does not have any pets.")
        }
        return petsMap
    }
}