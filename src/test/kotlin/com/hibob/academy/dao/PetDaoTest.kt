package com.hibob.academy.dao
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext)  {
    private val companyId = 9L
    private val ownerId = 123L
    private val dao = PetDao(sql)
    val pet = PetTable.instance
    private val petWithoutOwner = Pet("George",PetType.CAT,LocalDate.now(), companyId, null)
    private val waffle = Pet("Waffle",PetType.DOG, LocalDate.now(), companyId, ownerId)


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(pet).where(pet.companyId.eq(companyId)).execute()
    }

    @Test
    fun `Insert new pet to DB And Checking the function that get all pets by type`() {
        // Insert the new pet into the database and get the serial id
        val newPetSerialId = dao.insertNewPet(waffle)
        assertTrue(newPetSerialId != -1L, "Test failed: Pet not added successfully!")

        //Get all pets by the type -->DOG
        val petsWithDogType = dao.getAllPetsByType(PetType.DOG,companyId)

        // Assert that the filtered list is not empty, meaning the pet exists
        assertNotNull(petsWithDogType.filter { pet-> pet.petId == newPetSerialId }, "Test failed: Pet Waffle should have been added to the database")
    }

    @Test
    fun `Update pet owner id if the pet has no owner`(){
        // Step 1: Insert a new pet without an owner
        val newPetSerialId = dao.insertNewPet(petWithoutOwner)

        // Step 2: Update the pet's ownerId
        assertEquals(1, dao.updatePetOwnerId(newPetSerialId, ownerId, companyId), "Test failed: Update pet owner id failed!")

        // Step 3: Fetch the updated pet
        val updatedPet = dao.getPetById(newPetSerialId,companyId)

        // Step 4: Check if the pet was found and not null
        assertNotNull(updatedPet, "Test failed: The pet was not found after insertion.")

        // Step 5: Verify that the ownerId was updated
        assertEquals(ownerId, updatedPet!!.ownerId, "Test failed: The pet's ownerId should be updated.")
    }


    @Test
    fun `test GetAllPets function`() {
        // Insert two pets for the same company
        val pet1Id = dao.insertNewPet(Pet("Waffle",PetType.DOG, LocalDate.now(), companyId, null))
        val pet2Id = dao.insertNewPet(Pet("Mittens",PetType.CAT, LocalDate.now(), companyId, 5L))

        // Get all pets for the company
        val pets = dao.getAllPets(companyId)

        // Check the result
        assertEquals(2, pets.size, "Test failed: Incorrect number of pets returned")
        assertTrue(pets.any { it.petId == pet1Id }, "Test failed: Pet 1 not found")
        assertTrue(pets.any { it.petId == pet2Id }, "Test failed: Pet 2 not found")
    }
}