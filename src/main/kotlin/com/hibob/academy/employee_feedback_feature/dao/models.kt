package com.hibob.academy.employee_feedback_feature.dao

import java.time.LocalDateTime

data class FeedbackRequest(val feedbackText: String, val isAnonymous: Boolean, val department: String)
data class FeedbackSubmission(val employeeId: Long?, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String)
data class FeedbackData(val feedbackId: Long, val employeeId: Long?, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String, val createdAt: LocalDateTime, val status: Boolean)

data class EmployeeFullData(val id: Int, val firstName: String, val lastName: String, val role: EmployeeRole, val companyId: Int)
data class EmployeeData(val employeeId: Int, val role: EmployeeRole, val companyId: Int)
data class EmployeeLogin(val firstName: String, val lastName: String, val companyId: Int)

data class FeedbackResponseData(val responseId: Long, val responseText: String, val responderId: Long, val feedbackId: Long, val createdAt: LocalDateTime)
data class ResponseSubmission(val responseText: String, val responderId: Long, val feedbackId: Long)

enum class EmployeeRole{
    ADMIN,
    MANAGER,
    EMPLOYEE,
    HR;
}