package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.*
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Component

@Component
class FeedbackService @Autowired constructor(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(feedbackSubmission: FeedbackSubmission): Long {
        return feedbackDao.submitFeedback(feedbackSubmission)
    }

    fun getFeedbackHistory(companyId: Long, employeeId: Long): List<FeedbackData> {
        return feedbackDao.getFeedbackHistory(companyId, employeeId)
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        return feedbackDao.getAllFeedbacks(companyId)
    }
}