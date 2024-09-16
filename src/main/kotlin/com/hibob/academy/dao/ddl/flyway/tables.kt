package com.hibob.academy.dao.ddl.flyway

import com.hibob.academy.utils.JooqTable

class VaccineTable(tableName: String = "vaccine"): JooqTable(tableName){
    val id = createBigIntField("id")
    val name = createBigIntField("name") // Nullable Long for ownerId

    companion object{
        val  instance = VaccineTable()
    }
}


class VaccineToPetTable(tableName: String = "vaccineToPetTable"): JooqTable(tableName){
    val id = createBigIntField("id")
    val petId = createVarcharField("pet_id")
    val vaccinationDate = createBigIntField("vaccination_date")

    companion object{
        val instance = VaccineToPetTable()
    }
}