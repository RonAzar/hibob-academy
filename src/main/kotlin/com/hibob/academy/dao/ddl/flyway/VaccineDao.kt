package com.hibob.academy.dao.ddl.flyway

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record

@Component
class VaccineDao @Autowired constructor(private val sql: DSLContext) {
    private val vaccine = VaccineTable.instance

    private val vaccineDataMapper = RecordMapper<Record, VaccineData> { record ->
        VaccineData(
            record[vaccine.id],
            record[vaccine.name],
        )
    }
}