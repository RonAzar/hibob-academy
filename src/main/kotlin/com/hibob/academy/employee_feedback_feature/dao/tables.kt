package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.JooqTable

class FeedbackTable(tableName: String= "feedback"): JooqTable(tableName){
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val employeeId = createBigIntField("employee_id")
    val feedbackText = createVarcharField("feedback_text")
    val isAnonymous = createBooleanField("is_anonymous")
    val department = createVarcharField("department")
    val createdAt = createLocalDateTimeField("created_at")
    val status = createBooleanField("status")

    companion object{
        val instance = FeedbackTable()
    }
}