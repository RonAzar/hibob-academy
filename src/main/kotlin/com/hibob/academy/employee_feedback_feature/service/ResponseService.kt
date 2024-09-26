package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackResponseDao
import com.hibob.academy.employee_feedback_feature.dao.ResponseSubmission
import com.hibob.academy.employee_feedback_feature.validation.ValidateSubmission.Companion.validateTextSubmission
import jakarta.ws.rs.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ResponseService @Autowired constructor(
    private val responseDao: FeedbackResponseDao,
    private val feedbackDao: FeedbackDao
) {
    fun submitResponse(newFeedbackResponse: ResponseSubmission, employeeId: Long, companyId: Long): Long {
        validateTextSubmission(newFeedbackResponse.responseText, 300)
        val feedbackExists = feedbackDao.getFeedbackByFeedbackId(companyId, newFeedbackResponse.feedbackId)
        feedbackExists?.let {
            if (!feedbackExists.isAnonymous) {
                return responseDao.submitResponse(newFeedbackResponse, employeeId)
            }

            throw IllegalArgumentException("Sorry, this feedback is anonymous. You cannot respond to it!")
        }

        throw NotFoundException("Feedback with ID ${newFeedbackResponse.feedbackId} does not exist.")
    }
}