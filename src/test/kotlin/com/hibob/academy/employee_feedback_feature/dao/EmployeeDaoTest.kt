package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val employeeDao = EmployeeDao(sql)

    private val companyId = 1
    private val employeeFirstName = "Rachel"
    private val employeeLastName = "Green"
    private val employeeRole = EmployeeRole.ADMIN

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
