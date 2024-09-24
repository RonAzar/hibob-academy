package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class FeedbackServiceTest {

    private val companyId = 3L
    private val employeeId = 1L
    private val feedbackText = "What a feedback!"
    private val isAnonymous = false
    private val department = "Sales"
    private val feedbackDao = mock<FeedbackDao>()
    private val feedbackService = FeedbackService(feedbackDao)

    @Test
    fun `submitFeedback should submit feedback and return its ID`() {
        val feedbackId = 1L
        val feedbackSubmission = FeedbackSubmission(employeeId, companyId, feedbackText, isAnonymous, department)

        whenever(feedbackDao.submitFeedback(feedbackSubmission)).thenReturn(feedbackId)

        val result = feedbackService.submitFeedback(feedbackSubmission)

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
    fun `getAllFeedbacks should return all feedbacks when valid`() {
        val feedbackDataList = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)
        )

        whenever(feedbackDao.getAllFeedbacks(companyId)).thenReturn(feedbackDataList)

        val result = feedbackService.getAllFeedbacks(companyId)

        assertEquals(feedbackDataList, result)
    }
}