package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.JooqTable
import com.sun.java.swing.ui.CommonUI.createTextField

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

class FeedbackResponseTable(tableName: String = "feedback_response") : JooqTable(tableName){
    val id = createBigIntField("id")
    val responseText = createVarcharField("response_text")
    val responderId = createBigIntField("responder_id")
    val feedbackId = createBigIntField("feedback_id")
    val createdAt = createLocalDateTimeField("created_at")

    companion object{
        val instance = FeedbackResponseTable()
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
