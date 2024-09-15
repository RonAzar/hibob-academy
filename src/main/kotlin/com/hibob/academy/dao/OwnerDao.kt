package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class OwnerDao(private val sql: DSLContext) {
    private val owner = OwnerTable.instance
    private val pet = PetTable.instance

    private val ownerDataMapper = RecordMapper<Record,OwnerData>{ record ->
            OwnerData(
                record[owner.id],
                record[owner.ownerName],
                record[owner.employeeId],
                record[owner.companyId]
            )
    }

    //Create a function that retrieve all the owners from the owner table and return their name, employee id and company id
    fun getAllOwners(companyId: Long): List<OwnerData>{
        return sql.select(owner.id, owner.ownerName,owner.employeeId,owner.companyId)
            .from(owner)
            .where(owner.companyId.eq(companyId))
            .fetch(ownerDataMapper)
    }

//    Add a new API that get pet id and  return the owner’s information.
    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData? {
        return sql.select(owner.id, owner.ownerName, owner.employeeId, owner.companyId)
            .from(pet)
            .leftJoin(owner).on(pet.ownerId.eq(owner.id))  // Left join to include pets without an owner
            .where(pet.id.eq(petId))
            .and(pet.companyId.eq(companyId))
            .fetchOne(ownerDataMapper)  // Fetch a single record
    }

    fun getOwnerByEmployeeIdAndCompanyId(employeeId: String, companyId: Long): OwnerData?{
        return sql.select(owner.id, owner.ownerName,owner.employeeId,owner.companyId)
            .from(owner)
            .where(owner.companyId.eq(companyId))
            .and(owner.employeeId.eq(employeeId))
            .fetchOne(ownerDataMapper)
    }

    //Create a new function in the DAO file that create a new owner record (if it doesn’t exist already).
    fun insertNewOwner(ownerName: String, employeeId: String, companyId: Long): Long{
        return sql.insertInto(owner)
            .set(owner.ownerName, ownerName)
            .set(owner.employeeId,employeeId)
            .set(owner.companyId, companyId)
            .onConflict(owner.companyId, owner.employeeId)
            .doNothing()
            .returning(owner.id)  // Return the generated ID after insertion
            .fetchOne()         // Fetch the newly created row
            ?.get(owner.id) ?: -1    // Extract the ID from the row
    }
}