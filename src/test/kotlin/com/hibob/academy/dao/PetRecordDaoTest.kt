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
class PetRecordDaoTest @Autowired constructor(private val sql: DSLContext)  {
    private val companyId = 9L
    private val ownerId = 123L
    private val dao = PetDao(sql)
    val pet = PetTable.instance
    private val petWithoutOwner = PetRecord("George",PetType.CAT,LocalDate.now(), companyId, null)
    private val waffle = PetRecord("Waffle",PetType.DOG, LocalDate.now(), companyId, ownerId)


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
    fun `Update pet owner id should not update if ownerId already exists`() {
        // Step 1: Insert a new pet with an existing owner
        val newPetSerialId = dao.insertNewPet(waffle)

        // Step 2: Attempt to update the pet's ownerId (should not update because the pet already has an owner)
        val rowsAffected = dao.updatePetOwnerId(newPetSerialId, ownerId + 1, companyId)

        // Step 3: Verify that no rows were affected (since the pet already has an owner)
        assertEquals(0, rowsAffected, "Test failed: OwnerId should not be updated if pet already has an owner")

        // Step 4: Fetch the pet and verify that the ownerId has not changed
        val petAfterUpdate = dao.getPetById(newPetSerialId, companyId)
        assertEquals(ownerId, petAfterUpdate!!.ownerId, "Test failed: The pet's ownerId should remain unchanged")
    }

    @Test
    fun `Update pet owner id should fail for non-existent pet`() {
        // Step 1: Attempt to update the ownerId of a non-existent pet (invalid petId)
        val rowsAffected = dao.updatePetOwnerId(-1L, ownerId, companyId)

        // Step 2: Verify that no rows were affected (since the pet doesn't exist)
        assertEquals(0, rowsAffected, "Test failed: Update should fail for non-existent petId")
    }

    @Test
    fun `Get pet by invalid petId should return null`() {
        // Step 1: Try to fetch a pet with an invalid petId
        val fetchedPet = dao.getPetById(-1L, companyId)

        // Step 2: Verify that the result is null
        assertNull(fetchedPet, "Test failed: Fetching a non-existent pet should return null")
    }


    @Test
    fun `test GetAllPets function`() {
        // Insert two pets for the same company
        val pet1Id = dao.insertNewPet(PetRecord("Waffle",PetType.DOG, LocalDate.now(), companyId, null))
        val pet2Id = dao.insertNewPet(PetRecord("Mittens",PetType.CAT, LocalDate.now(), companyId, 5L))

        // Get all pets for the company
        val pets = dao.getAllPets(companyId)

        // Check the result
        assertEquals(2, pets.size, "Test failed: Incorrect number of pets returned")
        assertTrue(pets.any { it.petId == pet1Id }, "Test failed: Pet 1 not found")
        assertTrue(pets.any { it.petId == pet2Id }, "Test failed: Pet 2 not found")
    }
}