package com.hibob.academy.dao.ddl.flyway

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record

@Component
class VaccineToPetDao @Autowired constructor(private val sql: DSLContext) {
    private val vaccineToPet = VaccineToPetTable.instance

    private val vaccineToPetDataMapper = RecordMapper<Record, VaccineToPetData> { record ->
        VaccineToPetData(
            record[vaccineToPet.id],
            record[vaccineToPet.petId],
            record[vaccineToPet.vaccinationDate]
        )
    }
}