package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackData
import com.hibob.academy.employee_feedback_feature.dao.FeedbackResponseDao
import com.hibob.academy.employee_feedback_feature.dao.ResponseSubmission
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class ResponseServiceTest {
    private val companyId = -3L
    private val feedbackId = -3L
    private val responseText = "OMG this is the best response ever!"
    private val responderId = -2L
    private val responseId = -4L

    private val feedbackDao = mock<FeedbackDao>()
    private val responseDao = mock<FeedbackResponseDao>()
    private val responseService = ResponseService(responseDao, feedbackDao)

    @Test
    fun `submitResponse should submit response and return its ID`() {
        val testFeedbackData = FeedbackData(
            feedbackId = feedbackId,
            employeeId = responderId,
            companyId = companyId,
            feedbackText = "What a feedback!",
            isAnonymous = false,
            department = "Sales",
            createdAt = LocalDateTime.now(),
            status = true
        )

        whenever(feedbackDao.getFeedbackByFeedbackId(companyId, feedbackId)).thenReturn(testFeedbackData)
        whenever(responseDao.submitResponse(any(), any())).thenReturn(responseId)

        val responseSubmission = ResponseSubmission(responseText, feedbackId)
        val result = responseService.submitResponse(responseSubmission, responseId, companyId)

        assertEquals(responseId, result)
    }

    @Test
    fun `submitResponse should throw an exception if feedback does not exist`() {
        whenever(feedbackDao.getFeedbackByFeedbackId(companyId, feedbackId)).thenReturn(null)

        val responseSubmission = ResponseSubmission(responseText, feedbackId)

        val exception = assertThrows<NotFoundException> {
            responseService.submitResponse(responseSubmission, responseId, companyId)
        }

        assertEquals("Feedback with ID $feedbackId does not exist.", exception.message)
    }
}