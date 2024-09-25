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

    fun getFeedbacksUsingFilter(filter: FeedbackFilter): List<FeedbackData> {
        return feedbackDao.getFeedbacksUsingFilter(filter)
    }

    fun getFeedbackStatus(searchedFeedback: SearchedFeedback): Boolean {
        return feedbackDao.getFeedbackStatus(searchedFeedback).status
    }

    fun updateFeedbackStatus(updateFeedback: UpdateFeedbackStatus): String {
        val rowsAffected = feedbackDao.updateFeedbackStatus(updateFeedback)
        return if (rowsAffected > 0) {
            "Feedback status updated!"
        } else {
            "Feedback status not updated!"
        }
    }
}