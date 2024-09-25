package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackResponseDao
import com.hibob.academy.employee_feedback_feature.dao.ResponseSubmission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ResponseService @Autowired constructor(
    private val responseDao: FeedbackResponseDao,
    private val feedbackDao: FeedbackDao
) {
    fun submitResponse(newFeedbackResponse: ResponseSubmission, companyId: Long): Long {
        val feedbackExists = feedbackDao.getFeedbackByFeedbackId(companyId, newFeedbackResponse.feedbackId)
        feedbackExists?.let {
            return responseDao.submitResponse(newFeedbackResponse)
        }

        throw IllegalArgumentException("Feedback with ID ${newFeedbackResponse.feedbackId} does not exist.")
    }
}