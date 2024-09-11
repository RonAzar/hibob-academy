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
    fun getAllPetsByType(type: TYPE): List<PetData> {
        return sql.select(pet.petName, pet.dateOfArrival, pet.companyId, pet.petType)  // Include petType
            .from(pet)
            .where(pet.petType.eq(type.name.lowercase()))
            .fetch(petDataMapper)
    }

    fun createNewPet(newPet: PetData) {
        sql.insertInto(pet)
            .set(pet.petName, newPet.petName)
            .set(pet.dateOfArrival, newPet.dateOfArrival)
            .set(pet.petType, TYPE.DOG.name.lowercase())  // Set the type dynamically
            .set(pet.companyId, newPet.companyId)
            .onConflict(pet.companyId)
            .doNothing()
            .execute()
    }
}