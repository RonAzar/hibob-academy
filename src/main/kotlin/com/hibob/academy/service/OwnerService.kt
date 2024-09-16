package com.hibob.academy.service

import com.hibob.academy.dao.Owner
import com.hibob.academy.dao.OwnerDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OwnerService @Autowired constructor(private val ownerDao: OwnerDao) {
    fun insertOwner(newOwner: Owner) = ownerDao.insertNewOwner(newOwner)
    fun getOwnerByEmployeeIdAndCompanyId(employeeId: String, companyId: Long) = ownerDao.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId)
    fun getAllOwners(companyId: Long) = ownerDao.getAllOwners(companyId)
    fun getOwnerByPetId(petId: Long, companyId: Long) = ownerDao.getOwnerByPetId(petId, companyId)
}