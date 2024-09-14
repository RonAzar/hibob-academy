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
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext)  {
    val owner = OwnerTable.instance
    private val ownerRon = OwnerData(123,"Ron Azar", "314444444", 9)
    private val companyId = 9L
    private val dao = OwnerDao(sql)

//±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±
    //used for my Get owner details by pet id and owner id Test
    private val daoPet = PetDao(sql)
    private val pet = PetTable.instance
//±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(owner).where(owner.companyId.eq(companyId)).execute()
        sql.deleteFrom(pet).where(pet.companyId.eq(companyId)).execute()
    }



    @Test
    fun `Insert new owner to DB`() {
        // Insert the new owner into the database
        dao.insertNewOwner(ownerRon.ownerName, ownerRon.employeeId, ownerRon.companyId)

        // Fetch all owners from the database
        val owners = dao.getAllOwners(companyId)

        val addedOwner = dao.getOwnerByEmployeeIdAndCompanyId(ownerRon.employeeId, ownerRon.companyId)

        // Verify that the owner has been added by checking if ownerRon is in the list of all owners
        assertTrue(addedOwner in owners, "Test failed: Owner should have been added to the database")
    }

    @Test
    fun `Get owner details by pet Id and owner Id`(){
        dao.insertNewOwner(ownerRon.ownerName, ownerRon.employeeId, ownerRon.companyId)
        val addedOwner = dao.getOwnerByEmployeeIdAndCompanyId(ownerRon.employeeId, ownerRon.companyId)

        //Check if the ownerId was found and not null
        assertNotNull(addedOwner?.ownerId, "Test failed: The owner was not added to the database.")

        val pet = PetData(-2,"Waffle" , LocalDate.now(), companyId, PetType.DOG, addedOwner!!.ownerId)

        // Step 5: Insert the new pet into the database
        val newPetSerialId = daoPet.insertNewPet(pet.petName, pet.dateOfArrival,pet.petType,pet.companyId,pet.ownerId)
        assertTrue(newPetSerialId != -1L, "Test failed: Pet not added successfully!")

        // Step 6: Fetch the owner's details using the petId
        val fetchedOwner = dao.getOwnerByPetId(newPetSerialId, pet.companyId)

        // Step 7: Assert that the fetched owner's details match the original owner's details
        assertNotNull(fetchedOwner, "Test failed: No owner found for the given pet.")
        assertEquals(fetchedOwner,addedOwner, "Test failed: No owner found for the given pet.")
    }
}