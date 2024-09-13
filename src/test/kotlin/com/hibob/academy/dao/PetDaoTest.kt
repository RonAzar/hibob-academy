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
    private val petWithoutOwner = PetData(34,"George",LocalDate.now(), companyId, PetType.CAT, null)
    private val waffle = PetData(-2,"Waffle",LocalDate.now(), companyId, PetType.DOG, ownerId)


    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(pet).where(pet.companyId.eq(companyId)).execute()
    }

    @Test
    fun `Insert new pet to DB`() {
        // Insert the new pet into the database
        dao.insertNewPet(waffle)

        // Use filter to find matching pets and check if the list is not empty
        val petsWithDogType = dao.getAllPetsByType(PetType.DOG,companyId)

        val petInsideDataBase = dao.getPetByDetails(waffle)

        // Assert that the filtered list is not empty, meaning the pet exists
        assertTrue(petInsideDataBase in petsWithDogType, "Test failed: Pet Waffle should have been added to the database")
    }


    @Test
    fun `Update pet owner id if the pet has no owner`(){
        // Step 1: Insert a new pet without an owner
        dao.insertNewPet(petWithoutOwner)

        val petIdInDataBase = dao.getPetByDetails(petWithoutOwner)

        // Step 2: Update the pet's ownerId
        dao.updatePetOwnerId(petIdInDataBase!!.petId, ownerId, companyId)

        // Step 3: Fetch the updated pet
        val updatedPet = dao.getPetByDetails(petWithoutOwner)

        // Step 4: Check if the pet was found and not null
        assertNotNull(updatedPet, "Test failed: The pet was not found after insertion.")

        // Step 5: Verify that the ownerId was updated
        assertEquals(ownerId, updatedPet!!.ownerId, "Test failed: The pet's ownerId should be updated.")
    }
}