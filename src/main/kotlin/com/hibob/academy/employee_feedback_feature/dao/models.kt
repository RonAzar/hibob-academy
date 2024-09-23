package com.hibob.academy.employee_feedback_feature.dao

import java.time.LocalDateTime

data class FeedbackSubmission(val employeeId: Long, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String)
data class FeedbackData(val feedbackId: Long, val employeeId: Long?, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String, val createdAt: LocalDateTime, val status: Boolean)

data class EmployeeData(val employeeId: Int, val role: EmployeeRole, val companyId: Int)
data class EmployeeLogin(val firstName: String, val lastName: String, val companyId: Int)

enum class EmployeeRole(val role: String) {
    ADMIN("admin"),
    MANAGER("manager"),
    EMPLOYEE("employee"),
    HR("hr");

    companion object {
        fun fromString(role: String): EmployeeRole? {
            return entries.find { it.role.equals(role, ignoreCase = true) }
        }
    }
}