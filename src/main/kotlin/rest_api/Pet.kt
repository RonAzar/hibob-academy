package rest_api

import java.sql.Date

data class Pet(val petId: Long, val name: String, val type: String, val companyId: Int, val dateOfArrival: Date)