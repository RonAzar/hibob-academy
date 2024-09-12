package com.hibob.academy.dao

import org.jooq.Record
import org.jooq.DSLContext
import org.jooq.RecordMapper


class PetDao(private val sql: DSLContext) {
    private val pet = PetTable.instance
    private val petDataMapper= RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.petName],
            record[pet.dateOfArrival],
            record[pet.companyId]
        )
    }

    //Create a function that retrieve all the pets by a given type (represented by enum) and return their name, dateOfArrival and company Id
    fun getAllPetsByType(type: PetType, companyId: Long): List<PetData> {
        return sql.select(pet.petName, pet.dateOfArrival, pet.companyId, pet.petType)  // Include petType and companyId
            .from(pet)
            .where(pet.petType.eq(type.name))
            .and(pet.companyId.eq(companyId))  // Add companyId condition
            .fetch(petDataMapper)
    }

//    Add an API that will receive a pet id and owner id
//    Update the pet with the ownerID
//    What should you do if the pet already have an owner Id?
    fun updatePetOwnerId(petId: Long, petOwnerId: String) {

    }


    fun createNewPet(newPet: PetData) {
        sql.insertInto(pet)
            .set(pet.petName, newPet.petName)
            .set(pet.dateOfArrival, newPet.dateOfArrival)
            .set(pet.petType, PetType.DOG.name)  // Set the type dynamically
            .set(pet.companyId, newPet.companyId)
            .execute()
    }
}