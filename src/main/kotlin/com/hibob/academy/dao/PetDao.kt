package com.hibob.academy.dao

import org.jooq.Record
import org.jooq.DSLContext
import org.jooq.RecordMapper


class PetDao(private val sql: DSLContext) {
    private val pet = PetTable.instance
    private val petDataMapper= RecordMapper<Record, PetData> { record ->
        PetData(
            record[pet.petId],
            record[pet.petName],
            record[pet.petType]
        )
    }

    //fun get
}