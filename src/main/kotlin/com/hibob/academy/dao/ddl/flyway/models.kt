package com.hibob.academy.dao.ddl.flyway

import java.time.LocalDate

data class VaccineData(val vaccineId: Long, val vaccineName: String)
data class Vaccine(val vaccineName: String)

data class VaccineToPetData(val vaccineToPetId: Long, val petId: Long, val vaccinationDate: LocalDate)
data class VaccineToPet(val petId: Long,  val vaccinationDate: LocalDate)
