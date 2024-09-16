package com.hibob.academy.service

import com.hibob.academy.dao.Owner
import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.OwnerData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OwnerService @Autowired constructor(private val ownerDao: OwnerDao) {
    fun insertOwner(newOwner: Owner): Long {
        return ownerDao.insertNewOwner(newOwner)
    }
    fun getOwnerByEmployeeIdAndCompanyId(employeeId: String, companyId: Long): OwnerData?{
        val owner = ownerDao.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId) ?: throw IllegalArgumentException("No owner with id $employeeId found")
        return owner
    }
    fun getAllOwners(companyId: Long): List<OwnerData> {
        return ownerDao.getAllOwners(companyId)
    }
    fun getOwnerByPetId(petId: Long, companyId: Long): OwnerData? {
        val owner = ownerDao.getOwnerByPetId(petId, companyId) ?: throw IllegalArgumentException("Pet $petId has no owner with company id $companyId")
        return owner
    }
}