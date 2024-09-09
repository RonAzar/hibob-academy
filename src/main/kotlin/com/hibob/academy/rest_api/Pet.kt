package com.hibob.academy.rest_api

import java.util.Date

data class Pet(val petId: Long, val name: String?, val firstName: String?, val lastName: String?, val type: String, val companyId: Int, val dateOfArrival: Date)