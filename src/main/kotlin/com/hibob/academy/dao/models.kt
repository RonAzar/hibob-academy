package com.hibob.academy.dao

import java.time.LocalDate

data class PetData(val petId: Long ,val petName: String, val dateOfArrival: LocalDate, val companyId: Long, val petType: PetType, val ownerId: Long?)
data class Pet(val petName: String, val petType: PetType, val dateOfArrival: LocalDate,val companyId: Long, val ownerId: Long?)

//return their name, employee id and company id.
data class OwnerData(val ownerId: Long,val ownerName: String, val employeeId: String, val companyId: Long)
data class Owner(val ownerName: String, val employeeId: String, val companyId: Long)

//pets by a given type (represented by enum)
enum class PetType{
    DOG,
    CAT,
    HAMSTER,
    RABBIT
}

data class Example(val id: Long, val companyId: Long, val data: String)