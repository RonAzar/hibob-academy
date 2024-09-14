package com.hibob.academy.service

data class Owner(val ownerName: String,val employeeId: String, val companyId: Long)
data class CompanyRequest(val companyId: Long)