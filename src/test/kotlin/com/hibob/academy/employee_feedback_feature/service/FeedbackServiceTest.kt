package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackData
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class FeedbackServiceTest{
    private val companyId = 3L
    private val employeeId = 1L
    private val feedbackText = "What a feedback!"
    private val isAnonymous = false
    private val department = "Sales"
    private val testFeedback = FeedbackSubmission(employeeId, companyId, feedbackText, isAnonymous, department)
    private val feedbackDao = mock<FeedbackDao>()
    private val feedbackService = FeedbackService(feedbackDao)

    @Test
    fun `submitFeedback should submit feedback and return its ID`() {
        val feedbackId = 1L
        whenever(feedbackDao.submitFeedback(testFeedback)).thenReturn(feedbackId)

        val result = feedbackService.submitFeedback(testFeedback)

        assertEquals(feedbackId, result)
    }

    @Test
    fun `getFeedbackHistory should return feedback history when valid`() {
        val feedbackDataList = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)
        )

        whenever(feedbackDao.getFeedbackHistory(companyId, employeeId)).thenReturn(feedbackDataList)

        val result = feedbackService.getFeedbackHistory(companyId, employeeId)

        assertEquals(feedbackDataList, result)
    }

    @Test
    fun `getFeedbackHistory should throw 404 Not Found when employee does not exist`() {
        // To be implemented: Check if employee exists in the database; mock this behavior and assert NotFound exception.
    }

    @Test
    fun `getFeedbackHistory should throw 401 Unauthorized if employee is not authorized`() {
        // To be implemented: Check authentication of employee/admin/hr; mock this behavior and assert Unauthorized exception.
        // Waiting for login method...
    }

    @Test
    fun `getAllFeedbacks should return all feedbacks when authorized`() {
        val feedbackDataList = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)
        )

        whenever(feedbackDao.getAllFeedbacks(companyId)).thenReturn(feedbackDataList)

        val result = feedbackService.getAllFeedbacks(companyId)

        assertEquals(feedbackDataList, result)
    }

    @Test
    fun `getAllFeedbacks should throw 401 Unauthorized if employee is not authorized`() {
        // To be implemented: Mock unauthorized access for admin/hr checks and assert Unauthorized exception.
        // Waiting for login method...
    }
}