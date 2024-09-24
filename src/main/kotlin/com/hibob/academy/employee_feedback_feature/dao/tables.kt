package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.JooqTable

class FeedbackTable(tableName: String = "feedback") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val employeeId = createBigIntField("employee_id")
    val feedbackText = createVarcharField("feedback_text")
    val isAnonymous = createBooleanField("is_anonymous")
    val department = createVarcharField("department")
    val createdAt = createLocalDateTimeField("created_at")
    val status = createBooleanField("status")

    companion object {
        val instance = FeedbackTable()
    }
}

class EmployeesTable(tableName: String = "employees") : JooqTable(tableName) {
    val id = createIntField("id")
    val firstName = createVarcharField("first_name")
    val lastName = createVarcharField("last_name")
    val role = createVarcharField("role")
    val companyId = createIntField("company_id")

    companion object {
        val instance = EmployeesTable()
    }
}

class CompanyTable(tableName: String = "company") : JooqTable(tableName) {
    val id = createIntField("id")
    val name = createVarcharField("name")

    companion object {
        val instance = CompanyTable()
    }
}
