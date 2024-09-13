package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record

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

    //Create a function that retrieve all the owners from the owner table and return their name, employee Id and company Id
    fun getAllOwners(companyId: Long): List<OwnerData>{
        return sql.select(owner.id, owner.ownerName,owner.employeeId,owner.companyId)
            .from(owner)
            .where(owner.companyId.eq(companyId))
            .fetch(ownerDataMapper)
    }

//    Add a new API that get pet id and  return the owner’s information.
    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData?{
        return sql.select(owner.id, owner.ownerName,owner.employeeId,owner.companyId)
            .from(pet)
            .join(owner).on(pet.ownerId.eq(owner.id))
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
    fun createNewOwner(newOwner: OwnerData){
        sql.insertInto(owner)
            .set(owner.ownerName, newOwner.ownerName)
            .set(owner.employeeId, newOwner.employeeId)
            .set(owner.companyId, newOwner.companyId)
            .onConflict(owner.companyId, owner.employeeId)
            .doNothing()
            .execute()
    }
}