package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)

    private val companyName = "Test Company"
    private val employeeFirstName = "Ron"
    private val employeeLastName = "Azar"
    private val employeeRole = EmployeeRole.ADMIN
    private var companyId: Int = 0
    private var employeeId: Int = 0

    @BeforeEach
    fun setUp() {
        // Add the company before each test
        companyId = employeeDao.addCompany(companyName)

        // Add the employee before each test
        val employeeData = EmployeeFullData(
            id = 0,
            firstName = employeeFirstName,
            lastName = employeeLastName,
            role = employeeRole,
            companyId = companyId
        )
        employeeId = employeeDao.addEmployee(employeeData)
    }

    @AfterEach
    fun tearDown() {
        // Delete the employee and company after each test
        employeeDao.deleteEmployee(employeeId)
        employeeDao.deleteCompany(companyId)
    }

    @Test
    fun `login should return employee data when correct details are provided`() {
        val loginEmployee = EmployeeLogin(employeeFirstName, employeeLastName, companyId)

        val result = employeeDao.authenticateEmployee(loginEmployee)

        assertNotNull(result)
        assertEquals(companyId, result.companyId)
        assertEquals(employeeRole, result.role)
    }

    @Test
    fun `login should throw exception when incorrect details are provided`() {
        val loginEmployee = EmployeeLogin("WrongName", "WrongLastName", companyId)

        val exception = assertThrows<IllegalArgumentException> {
            employeeDao.authenticateEmployee(loginEmployee)
        }

        assertEquals("User not found: Incorrect details provided", exception.message)
    }
}