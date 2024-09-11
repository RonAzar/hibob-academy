package com.hibob.academy.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record

class OwnerDao(private val sql: DSLContext) {
    private val owner = OwnerTable.instance
    private val ownerDataMapper = RecordMapper<Record,OwnerData>{ record ->
            OwnerData(
                record[owner.ownerName],
                record[owner.employeeId],
                record[owner.companyId]
            )
    }

    //Create a function that retrieve all the owners from the owner table and return their name, employee Id and company Id
    fun getAllOwners(): List<OwnerData>{
        return sql.select(owner.ownerName,owner.employeeId,owner.companyId)
            .from(owner)
            .fetch(ownerDataMapper)
    }
}