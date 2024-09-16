package com.hibob.academy.service

import com.hibob.academy.dao.Pet
import com.hibob.academy.dao.PetDao
import com.hibob.academy.dao.PetData
import com.hibob.academy.dao.PetType
import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class PetServiceTest{
    @Mock
    private lateinit var petDao: PetDao

    @InjectMocks
    private lateinit var petService: PetService

    private val companyId = 9L
    private val petId = 1L
    private val ownerId = 123L
    private val petData = PetData(petId, "Waffle", LocalDate.now(), companyId, PetType.DOG, null)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getAllPets should return list of pets`() {
        // Arrange
        whenever(petDao.getAllPets(companyId)).thenReturn(listOf(petData))

        // Act
        val result = petService.getAllPets(companyId)

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Waffle", result[0].petName)
        verify(petDao, times(1)).getAllPets(companyId)
    }

    @Test
    fun `insertNewPet should return new pet ID`() {
        // Arrange
        whenever(petDao.insertNewPet(org.mockito.kotlin.any(Pet))).thenReturn(10L)

        // Act
        val newPetId = petService.insertNewPet(Pet("Waffle", PetType.DOG, LocalDate.now(), companyId, null))

        // Assert
        assertEquals(10L, newPetId)
        verify(petDao, times(1)).insertNewPet(any(Pet::class.java))
    }

    @Test
    fun `getPetById should return pet when pet exists`() {
        // Arrange
        `when`(petDao.getPetById(petId, companyId)).thenReturn(petData)

        // Act
        val result = petService.getPetById(petId, companyId)

        // Assert
        assertNotNull(result)
        assertEquals("Waffle", result!!.petName)
        verify(petDao, times(1)).getPetById(petId, companyId)
    }

    @Test
    fun `getPetById should throw exception when pet does not exist`() {
        // Arrange
        `when`(petDao.getPetById(petId, companyId)).thenReturn(null)

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            petService.getPetById(petId, companyId)
        }
        assertEquals("Pet not found for the given ID", exception.message)
        verify(petDao, times(1)).getPetById(petId, companyId)
    }

    @Test
    fun `updatePetOwnerId should update owner ID successfully`() {
        // Arrange
        val petWithoutOwner = petData.copy(ownerId = null)
        `when`(petDao.getPetById(petId, companyId)).thenReturn(petWithoutOwner)
        `when`(petDao.updatePetOwnerId(petId, ownerId, companyId)).thenReturn(1)

        // Act
        val result = petService.updatePetOwnerId(petId, ownerId, companyId)

        // Assert
        assertEquals("Pet owner ID updated successfully", result)
        verify(petDao, times(1)).getPetById(petId, companyId)
        verify(petDao, times(1)).updatePetOwnerId(petId, ownerId, companyId)
    }

    @Test
    fun `updatePetOwnerId should throw exception if pet already has an owner`() {
        // Arrange
        val petWithOwner = petData.copy(ownerId = 45L)
        `when`(petDao.getPetById(petId, companyId)).thenReturn(petWithOwner)

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }
        assertEquals("Pet already has an owner", exception.message)
        verify(petDao, times(1)).getPetById(petId, companyId)
        verify(petDao, never()).updatePetOwnerId(petId, ownerId, companyId)
    }

    @Test
    fun `updatePetOwnerId should throw exception if pet is not found`() {
        // Arrange
        `when`(petDao.getPetById(petId, companyId)).thenReturn(null)

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            petService.updatePetOwnerId(petId, ownerId, companyId)
        }
        assertEquals("Pet not found for the given ID", exception.message)
        verify(petDao, times(1)).getPetById(petId, companyId)
        verify(petDao, never()).updatePetOwnerId(petId, ownerId, companyId)
    }
}