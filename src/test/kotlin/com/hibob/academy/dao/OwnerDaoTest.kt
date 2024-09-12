package com.hibob.academy.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext)  {
    private val companyId = 9L
    private val dao = OwnerDao(sql)
    val owner = OwnerTable.instance
    private val ownerRon = OwnerData("Ron Azar", "314444444", 9)

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(owner).where(owner.companyId.eq(companyId)).execute()
    }



    @Test
    fun `Insert new owner to DB`() {
        // Insert the new owner into the database
        dao.createNewOwner(ownerRon)

        // Fetch all owners from the database
        val owners = dao.getAllOwners(companyId)

        // Verify that the owner has been added by checking if ownerRon is in the list of all owners
        assertTrue(ownerRon in owners, "Test failed: Owner should have been added to the database")
    }
}