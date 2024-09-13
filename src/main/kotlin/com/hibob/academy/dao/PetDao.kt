package com.hibob.academy.dao

import org.jooq.Record
import org.jooq.DSLContext
import org.jooq.RecordMapper


class PetDao(private val sql: DSLContext) {
    private val pet = PetTable.instance

    private val PetDataMapper = RecordMapper<Record, PetData>{ record ->
        PetData(
            record[pet.id],
            record[pet.petName],
            record[pet.dateOfArrival],
            record[pet.companyId],
            PetType.valueOf(record[pet.petType].uppercase()),  // Convert string to enum (assuming your DB stores it as a string)
            record[pet.ownerId]
        )
    }

    //Create a function that retrieve all the pets by a given type (represented by enum) and return their name, dateOfArrival and company Id
    fun getAllPetsByType(type: PetType, companyId: Long): List<PetData> {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)  // Include petType and companyId
            .from(pet)
            .where(pet.petType.eq(type.name))
            .and(pet.companyId.eq(companyId))  // Add companyId condition
            .fetch(PetDataMapper)
    }

//    Add an API that will receive a pet id and owner id
//    Update the pet with the ownerID
//    What should you do if the pet already have an owner Id?
    fun updatePetOwnerId(petId: Long, petOwnerId: Long, companyId: Long) {
        sql.update(pet)
            .set(pet.ownerId, petOwnerId)
            .where(pet.companyId.eq(companyId))
            .and(pet.id.eq(petId))
            .and(pet.ownerId.isNull())
            .execute()
    }

    fun getAllPets(companyId: Long): List<PetData> {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)
            .from(pet)
            .where(pet.companyId.eq(companyId))
            .fetch(PetDataMapper)
    }



    fun getPetByDetails(petInfo: PetData): PetData? {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)
            .from(pet)  // Make sure to include 'from' in the query
            .where(pet.petName.eq(petInfo.petName))
            .and(pet.dateOfArrival.eq(petInfo.dateOfArrival))
            .and(pet.companyId.eq(petInfo.companyId))
            .and(pet.petType.eq(petInfo.petType.name))
            .fetchOne(PetDataMapper)  // Use 'fetchOne' since we expect one result
    }

    fun insertNewPet(newPet: PetData) {
        sql.insertInto(pet)
            .set(pet.petName, newPet.petName)
            .set(pet.dateOfArrival, newPet.dateOfArrival)
            .set(pet.petType, newPet.petType.name)
            .set(pet.companyId, newPet.companyId)
            .set(pet.ownerId, newPet.ownerId)
            .execute()
    }
}