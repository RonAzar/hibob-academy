package com.hibob.academy.dao

import com.hibob.academy.utils.JooqTable

class PetTable(tableName: String = "pets"): JooqTable(tableName){
    val id = createBigIntField("id")
    val ownerId = createBigIntField("owner_id") // Nullable Long for ownerId
    val petName = createVarcharField("name")
    val petType = createVarcharField("type")
    val companyId = createBigIntField("company_id")
    val dateOfArrival = createLocalDateField("date_of_arrival")

    companion object{
        val  instance = PetTable()
    }
}


class OwnerTable(tableName: String = "owner"): JooqTable(tableName){
    val id = createBigIntField("id")
    val ownerName = createVarcharField("name")
    val companyId = createBigIntField("company_id")
    val employeeId = createVarcharField("employee_id")


    companion object{
        val instance = OwnerTable()
    }
}