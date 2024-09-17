package com.hibob.academy.unittests

import com.hibob.academy.dao.*
import com.hibob.academy.service.PetService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDate

class PetServiceTest {

    private val petId: Long = 123
    private val companyId: Long = 9
    private val ownerId: Long = 456
    private val pet = PetData(petId, "Waffle", LocalDate.now(), companyId, PetType.DOG, null)

    private val petDao = mock<PetDao>()
    private val petService = PetService(petDao)

    @Test
    fun `Test getPetById -- Pet not found`() {
        whenever(petDao.getPetById(petId, companyId)).thenReturn(null)

        val errorMessage = assertThrows<NoSuchElementException> {
            petService.getPetById(petId, companyId)
        }

        assertEquals("Pet not found for the given ID", errorMessage.message)
    }

    @Test
    fun `Test getPetById -- Pet found`() {
        whenever(petDao.getPetById(petId, companyId)).thenReturn(pet)

        val result = petService.getPetById(petId, companyId)

        assertNotNull(result)
        assertEquals(petId, result.petId)
        assertEquals("Waffle", result.petName)
    }

    @Test
    fun `Test insertNewPet -- Pet inserted successfully`() {
        val newPet = PetRecord("Waffle", PetType.DOG, LocalDate.now(), companyId, null)
        whenever(petDao.insertNewPet(newPet)).thenReturn(10L)

        val result = petService.insertNewPet(newPet)

        assertEquals(10L, result)
    }

    @Test
    fun `Test updatePetOwnerId -- Pet not found`() {
        whenever(petDao.getPetById(petId, companyId)).thenReturn(null)

        val errorMessage = assertThrows<NoSuchElementException> {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }

        assertEquals("Pet not found for the given ID", errorMessage.message)
        verify(petDao, never()).updatePetOwnerId(any(), any(), any())
    }

    @Test
    fun `Test updatePetOwnerId -- Pet already has an owner`() {
        val petWithOwner = pet.copy(ownerId = ownerId)
        whenever(petDao.getPetById(petId, companyId)).thenReturn(petWithOwner)

        val errorMessage = assertThrows<IllegalArgumentException> {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }

        assertEquals("Pet already has an owner", errorMessage.message)
        verify(petDao, never()).updatePetOwnerId(any(), any(), any())
    }

    @Test
    fun `Test updatePetOwnerId -- Owner ID updated successfully`() {
        // Arrange
        val petWithoutOwner = pet.copy(ownerId = null)
        whenever(petDao.getPetById(petId, companyId)).thenReturn(petWithoutOwner)
        whenever(petDao.updatePetOwnerId(petId, ownerId, companyId)).thenReturn(1)

        val result = petService.updatePetOwnerId(petId, ownerId, companyId)

        assertEquals("Pet owner ID updated successfully", result)
    }

    @Test
    fun `Test updatePetOwnerId -- Failed to update owner ID`() {
        val petWithoutOwner = pet.copy(ownerId = null)
        whenever(petDao.getPetById(petId, companyId)).thenReturn(petWithoutOwner)
        whenever(petDao.updatePetOwnerId(petId, ownerId, companyId)).thenReturn(0)

        val errorMessage = assertThrows<IllegalArgumentException> {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }

        assertEquals("Failed to update pet's owner. The pet may already have an owner.", errorMessage.message)
    }

    @Test
    fun `Test getAllPets -- Return list of pets`() {
        whenever(petDao.getAllPets(companyId)).thenReturn(listOf(pet))

        val result = petService.getAllPets(companyId)

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Waffle", result[0].petName)
    }

    @Test
    fun `Test getAllPetsByType -- Return list of pets by type`() {
        whenever(petDao.getAllPetsByType(PetType.DOG, companyId)).thenReturn(listOf(pet))

        val result = petService.getAllPetsByType(PetType.DOG, companyId)

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(PetType.DOG, result[0].petType)
    }
}