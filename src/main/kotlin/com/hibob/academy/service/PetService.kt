package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PetService @Autowired constructor(private val petDao: PetDao) {
    fun getAllPets(companyId: Long) = petDao.getAllPets(companyId)
    fun insertNewPet(newPet: Pet) = petDao.insertNewPet(newPet)
    fun getPetById(petId: Long, companyId: Long) = petDao.getPetById(petId, companyId)
    fun getAllPetsByType(type: PetType, companyId: Long) = petDao.getAllPetsByType(type, companyId)
    fun updatePetOwnerId(petId: Long, petOwnerId: Long, companyId: Long) = petDao.updatePetOwnerId(petId, petOwnerId, companyId)
}