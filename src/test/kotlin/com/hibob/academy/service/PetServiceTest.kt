package com.hibob.academy.service

import com.hibob.academy.dao.*
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
    private val ownerDao = mock<OwnerDao>()
    private val petService = PetService(petDao, ownerDao)

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

        // Act & Assert: Ensure that no exceptions are thrown
        assertDoesNotThrow {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }

        // Verify that the updatePetOwnerId was called with correct parameters
        verify(petDao).updatePetOwnerId(petId, ownerId, companyId)
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

    //SQL2 Service tests
    @Test
    fun `Test getPetsByOwnerId -- Owner ID is invalid`() {
        val invalidOwnerId = -1L

        val errorMessage = assertThrows<IllegalArgumentException> {
            petService.getPetsByOwnerId(invalidOwnerId, companyId)
        }

        assertEquals("Owner does not exist invalid owner id!", errorMessage.message)
    }


    @Test
    fun `Test getPetsByOwnerId -- Pets found`() {
        whenever(ownerDao.getAllOwners(companyId)).thenReturn(listOf(OwnerData(ownerId, "John", "E123", companyId)))
        whenever(petDao.getPetsByOwnerId(ownerId, companyId)).thenReturn(listOf(pet))

        val result = petService.getPetsByOwnerId(ownerId, companyId)

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Waffle", result[0].petName)
    }

    // Tests for petTypesAmount
    @Test
    fun `Test petTypesAmount -- No pets found`() {
        whenever(petDao.petTypesAmount(companyId)).thenReturn(emptyMap())

        val errorMessage = assertThrows<NoSuchElementException> {
            petService.petTypesAmount(companyId)
        }

        assertEquals("This company does not have any pets.", errorMessage.message)
    }

    @Test
    fun `Test petTypesAmount -- Pets found`() {
        val petTypesMap = mapOf(PetType.DOG to 3L, PetType.CAT to 2L)
        whenever(petDao.petTypesAmount(companyId)).thenReturn(petTypesMap)

        val result = petService.petTypesAmount(companyId)

        assertNotNull(result)
        assertEquals(3L, result[PetType.DOG])
        assertEquals(2L, result[PetType.CAT])
    }

}