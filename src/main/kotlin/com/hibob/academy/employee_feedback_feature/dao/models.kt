package com.hibob.academy.employee_feedback_feature.dao

import java.time.LocalDateTime

data class FeedbackSubmission(val employeeId: Long, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String)
data class FeedbackData(val feedbackId: Long, val employeeId: Long, val companyId: Long, val feedbackText: String, val isAnonymous: Boolean, val department: String, val createdAt: LocalDateTime, val status: Boolean)