package com.hibob.academy.service

import com.hibob.academy.dao.PetType
import java.time.LocalDate

data class Pet(val petName: String, val petDateOfArrival: LocalDate, val petType: PetType, val companyId: Long, val ownerId: Long?)