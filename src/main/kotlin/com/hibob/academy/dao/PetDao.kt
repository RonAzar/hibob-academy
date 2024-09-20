package com.hibob.academy.dao

import org.jooq.Record
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class PetDao(private val sql: DSLContext) {
    private val pet = PetTable.instance

    private val petDataMapper = RecordMapper<Record, PetData>{ record ->
        PetData(
            record[pet.id],
            record[pet.petName],
            record[pet.dateOfArrival],
            record[pet.companyId],
            PetType.valueOf(record[pet.petType].uppercase()),  // Convert string to enum (assuming your DB stores it as a string)
            record[pet.ownerId]
        )
    }

    //Create a function that retrieve all the pets by a given type (represented by enum) and return their name, dateOfArrival and company id
    fun getAllPetsByType(type: PetType, companyId: Long): List<PetData> {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)  // Include petType and companyId
            .from(pet)
            .where(pet.petType.eq(type.name))
            .and(pet.companyId.eq(companyId))  // Add companyId condition
            .fetch(petDataMapper)
    }

//    Add an API that will receive a pet id and owner id
//    Update the pet with the ownerID
//    What should you do if the pet already have an owner id?
fun updatePetOwnerId(petId: Long, petOwnerId: Long, companyId: Long): Int {
    // Perform the update only if ownerId is null and return the updated pet
    val rowsAffected = sql.update(pet)
        .set(pet.ownerId, petOwnerId)
        .where(pet.companyId.eq(companyId))
        .and(pet.id.eq(petId))
        .and(pet.ownerId.isNull())  // Update only if ownerId is null
        .execute()

    return rowsAffected
}

    fun getAllPets(companyId: Long): List<PetData> {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)
            .from(pet)
            .where(pet.companyId.eq(companyId))
            .fetch(petDataMapper)
    }



    fun getPetById(petId: Long, companyId: Long): PetData? {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)
            .from(pet)
            .where(pet.id.eq(petId))
            .and(pet.companyId.eq(companyId))
            .fetchOne(petDataMapper)  // Use 'fetchOne' since we expect one result
    }

    //Return new pet serial id or -1 if insertion failed!
    fun insertNewPet(newPet: PetRecord): Long {
        return sql.insertInto(pet)
            .set(pet.petName, newPet.petName)
            .set(pet.dateOfArrival, newPet.dateOfArrival)
            .set(pet.petType, newPet.petType.name)
            .set(pet.companyId, newPet.companyId)
            .set(pet.ownerId, newPet.ownerId)
            .returning(pet.id)  // Return the generated ID after insertion
            .fetchOne()!!  // Force unwrap, will throw NPE if null
            .get(pet.id)  // Extract the ID from the row
    }



    //SQL 2:
    fun getPetsByOwnerId(ownerId: Long, companyId: Long): List<PetData> {
        return sql.select(pet.id, pet.petName, pet.dateOfArrival, pet.companyId, pet.petType, pet.ownerId)
            .from(pet)
            .where(pet.ownerId.eq(ownerId))
            .and(pet.companyId.eq(companyId))
            .fetch(petDataMapper)
    }

    fun petTypesAmount(companyId: Long): Map<PetType, Long> {
        val count = DSL.count(pet.id)
        return sql.select(pet.petType, count)
            .from(pet)
            .where(pet.companyId.eq(companyId))
            .groupBy(pet.petType)
            .fetch()
            .intoMap(
                { record -> PetType.valueOf(record[pet.petType].toString())},
                { record -> record[count].toLong()})
    }
}