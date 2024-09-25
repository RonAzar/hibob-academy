package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun `getFeedbacksUsingFilter should return feedbacks when filters are applied`() {
        val feedbackFilter = FeedbackFilter(
            companyId = companyId,
            isAnonymous = true,
            department = "Sales",
            createdAt = LocalDateTime.now().minusDays(1)
        )
        val expectedFeedbacks = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, true, department, LocalDateTime.now(), true)
        )

        whenever(feedbackDao.getFeedbacksUsingFilter(feedbackFilter)).thenReturn(expectedFeedbacks)

        val result = feedbackService.getFeedbacksUsingFilter(feedbackFilter)

        assertEquals(expectedFeedbacks, result)
    }

    @Test
    fun `getFeedbackStatus should return feedback status when feedback exists`() {
        val searchedFeedback = SearchedFeedback(companyId = companyId, feedbackId = 1L)
        val feedbackData = FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)

        whenever(feedbackDao.getFeedbackStatus(searchedFeedback)).thenReturn(feedbackData)

        val result = feedbackService.getFeedbackStatus(searchedFeedback)

        assertTrue(result)
    }

    @Test
    fun `getFeedbackStatus should throw exception when feedback not found`() {
        val searchedFeedback = SearchedFeedback(companyId = companyId, feedbackId = 1L)

        whenever(feedbackDao.getFeedbackStatus(searchedFeedback)).thenThrow(IllegalArgumentException("Feedback not found"))

        val exception = assertThrows<IllegalArgumentException> {
            feedbackService.getFeedbackStatus(searchedFeedback)
        }

        assertEquals("Feedback not found", exception.message)
    }

    @Test
    fun `updateFeedbackStatus should return success message when status is updated`() {
        val updateFeedbackStatus = UpdateFeedbackStatus(companyId = companyId, feedbackId = 1L, status = true)

        whenever(feedbackDao.updateFeedbackStatus(updateFeedbackStatus)).thenReturn(1)

        val result = feedbackService.updateFeedbackStatus(updateFeedbackStatus)

        assertEquals("Feedback status updated!", result)
    }

    @Test
    fun `updateFeedbackStatus should return failure message when status is not updated`() {
        val updateFeedbackStatus = UpdateFeedbackStatus(companyId = companyId, feedbackId = 1L, status = true)

        whenever(feedbackDao.updateFeedbackStatus(updateFeedbackStatus)).thenReturn(0)

        val result = feedbackService.updateFeedbackStatus(updateFeedbackStatus)

        assertEquals("Feedback status not updated!", result)
    }

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