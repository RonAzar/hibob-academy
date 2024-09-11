package com.hibob.academy.dao

import java.time.LocalDate

//return their name, dateOfArrival and company Id
data class PetData(val petName: String, val dateOfArrival: LocalDate, val companyId: Long)

//return their name, employee Id and company Id.
data class OwnerData(val ownerName: String, val employeeId: Long, val companyId: Long)

//pets by a given type (represented by enum)
enum class TYPE{
    DOG,
    CAT,
    HAMSTER,
    RABBIT
}

data class Example(val id: Long, val companyId: Long, val data: String)