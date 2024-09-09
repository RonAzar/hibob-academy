package com.hibob.academy.rest_api

data class Owner(val ownerId: Long, val firstName: String?, val lastName: String?,val companyId: Long, val employeeId: Long)

data class OldOwner(val ownerId: Long, val name: String ,val companyId: Long, val employeeId: Long)
