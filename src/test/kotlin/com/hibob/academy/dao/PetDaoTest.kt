package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext)  {
    private val companyId = 9L
    private val dao = PetDao(sql)
    val pet = PetTable.instance
    private val petWaffle = PetData("Waffle" , LocalDate.now(), 9)

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(pet).where(pet.companyId.eq(companyId)).execute()
    }

    @Test
    fun `Insert new pet to DB`() {
        // Insert the new pet into the database
        dao.createNewPet(petWaffle)

        // Use filter to find matching pets and check if the list is not empty
        val filteredPets = dao.getAllPetsByType(TYPE.DOG).filter { pet ->
            pet.petName == petWaffle.petName &&
                    pet.companyId == petWaffle.companyId &&
                    pet.dateOfArrival == petWaffle.dateOfArrival
        }

        // Assert that the filtered list is not empty, meaning the pet exists
        assertTrue(filteredPets.isNotEmpty(), "Pet Waffle should have been added to the database")
    }
}