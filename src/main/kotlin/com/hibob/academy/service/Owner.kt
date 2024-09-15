package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import org.springframework.stereotype.Component

@Component
class OwnerService(private val ownerDao: OwnerDao) {
    data class Owner(val ownerName: String, val employeeId: String, val companyId: Long)
    fun insertOwner(ownerName: String, employeeId: String, companyId: Long) = ownerDao.insertNewOwner(ownerName, employeeId, companyId)
}