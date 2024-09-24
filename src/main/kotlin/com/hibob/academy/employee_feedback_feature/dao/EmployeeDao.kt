package com.hibob.academy.employee_feedback_feature.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EmployeeDao @Autowired constructor(private val sql: DSLContext) {

    private val employee = EmployeesTable.instance
    private val company = CompanyTable.instance

    private val employeeDataMapper = RecordMapper<Record, EmployeeData> { record ->
        EmployeeData(
            record[employee.id],
            EmployeeRole.valueOf(record[employee.role].uppercase()),
            record[employee.companyId]
        )
    }

    fun authenticateEmployee(loginEmployee: EmployeeLogin): EmployeeData {
        return sql.select(employee.id, employee.role, employee.companyId)
            .from(employee)
            .where(employee.companyId.eq(loginEmployee.companyId))
            .and(employee.firstName.eq(loginEmployee.firstName))
            .and(employee.lastName.eq(loginEmployee.lastName))
            .fetchOne(employeeDataMapper) ?: throw IllegalArgumentException("User not found: Incorrect details provided")
    }

    fun addEmployee(employeeData: EmployeeFullData): Int {
        return sql.insertInto(employee)
            .set(employee.firstName, employeeData.firstName)
            .set(employee.lastName, employeeData.lastName)
            .set(employee.role, employeeData.role.name.lowercase())
            .set(employee.companyId, employeeData.companyId)
            .returning(employee.id)
            .fetchOne()!!
            .get(employee.id)
    }

    fun deleteEmployee(employeeId: Int) {
        sql.deleteFrom(employee)
            .where(employee.id.eq(employeeId))
            .execute()
    }

    fun addCompany(name: String): Int {
        return sql.insertInto(company)
            .set(company.name, name)
            .returning(company.id)
            .fetchOne()!!
            .get(company.id)
    }

    fun deleteCompany(companyId: Int) {
        sql.deleteFrom(company)
            .where(company.id.eq(companyId))
            .execute()
    }
}