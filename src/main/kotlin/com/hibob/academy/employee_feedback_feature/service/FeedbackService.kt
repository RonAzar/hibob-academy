package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.*
import com.hibob.academy.employee_feedback_feature.validation.ValidateSubmission.Companion.validateTextSubmission
import jakarta.ws.rs.NotFoundException
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Component

@Component
class FeedbackService @Autowired constructor(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(feedbackSubmission: FeedbackSubmission): Long {
        validateTextSubmission(feedbackSubmission.feedbackText, 300)
        validateTextSubmission(feedbackSubmission.department, 100)

        return feedbackDao.submitFeedback(feedbackSubmission)
    }

    fun getFeedbackHistory(companyId: Long, employeeId: Long): List<FeedbackData> {
        return feedbackDao.getFeedbackHistory(companyId, employeeId)
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        return feedbackDao.getAllFeedbacks(companyId)
    }

    fun getFeedbacksUsingFilter(filter: FeedbackFilter, companyId: Long): List<FeedbackData> {
        return feedbackDao.getFeedbacksUsingFilter(filter, companyId)
    }

    fun getFeedbackStatus(searchedFeedback: SearchedFeedback): Boolean {
        return feedbackDao.getFeedbackStatus(searchedFeedback).status
    }

    fun updateFeedbackStatus(updateFeedback: UpdateFeedbackStatus, companyId: Long): String {
        val rowsAffected = feedbackDao.updateFeedbackStatus(updateFeedback, companyId)
        if (rowsAffected > 0) {
            return "Feedback status updated!"
        }

        throw NotFoundException("Error: Feedback status not updated! Make sure the feedback exists at your company")
    }
}