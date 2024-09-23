package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackData
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Component
class FeedbackService @Autowired constructor(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(newFeedback: FeedbackSubmission): Long {
        return feedbackDao.submitFeedback(newFeedback)
    }

    fun getFeedbackHistory(companyId: Long, employeeId: Long): List<FeedbackData> {
        //Need to check if employee exists in database- if not--> 404 Not Found
        //Need to check if employee authenticate admin/hr/current employee - if not--> 401 Unauthorized
        return feedbackDao.getFeedbackHistory(companyId, employeeId)
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        //Need to check if employee authenticate admin/hr - if not--> 401 Unauthorized
        return feedbackDao.getAllFeedbacks(companyId)
    }
}