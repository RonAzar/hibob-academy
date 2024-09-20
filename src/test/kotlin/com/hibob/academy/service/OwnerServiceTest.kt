package com.hibob.academy.service

import com.hibob.academy.dao.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class OwnerServiceTest{
    private val petId = 10L
    private val ownerId = 123L
    private val employeeId = "1234578"
    private val newOwner=  OwnerRecord("Ron", employeeId, 9)
    private val ownerData = OwnerData(ownerId,"Ron", employeeId, 9)
    private val companyId = 9L

    private val ownerDao = mock<OwnerDao>()
    private val ownerService = OwnerService(ownerDao)


    @Test
    fun `insert Owner -- insert successfully`()
    {
        whenever(ownerDao.insertNewOwner(newOwner)).thenReturn(10L)
        val resultOwnerId = ownerService.insertOwner(newOwner)
        assertEquals(10L, resultOwnerId)
    }

    @Test
    fun `get Owner By Employee Id And Company Id -- Success`(){
        whenever(ownerDao.getOwnerByEmployeeIdAndCompanyId(any(), any())).thenReturn(ownerData)

        val resultOwner = ownerService.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId)
        assertEquals(ownerData, resultOwner)
    }

    @Test
    fun `get Owner By Employee Id And Company Id -- Failure`(){
        whenever(ownerDao.getOwnerByEmployeeIdAndCompanyId(any(), any())).thenReturn(null)

        val errorMessage = assertThrows<NoSuchElementException> {
            ownerService.getOwnerByEmployeeIdAndCompanyId(employeeId, companyId)
        }
        assertEquals(errorMessage.message, "No owner with id $employeeId found")
    }

    @Test
    fun `get all owners - Success`(){
        whenever(ownerDao.getAllOwners(companyId)).thenReturn(listOf(ownerData))
        val result = ownerService.getAllOwners(companyId)
        assertNotNull(result)
        assertEquals(newOwner.ownerName, result[0].ownerName)
        assertEquals(1, result.size)
    }

    @Test
    fun `get Owner By Pet Id -- Success`() {
        whenever(ownerDao.getOwnerByPetId(petId, companyId)).thenReturn(ownerData)

        val resultOwner = ownerService.getOwnerByPetId(petId, companyId)

        assertEquals(ownerData, resultOwner)
    }

    @Test
    fun `get Owner By Pet Id -- Failure`() {
        whenever(ownerDao.getOwnerByPetId(petId, companyId)).thenReturn(null)

        val errorMessage = assertThrows<NoSuchElementException> {
            ownerService.getOwnerByPetId(petId, companyId)
        }

        assertEquals("Pet $petId has no owner with company id $companyId", errorMessage.message)
    }
}