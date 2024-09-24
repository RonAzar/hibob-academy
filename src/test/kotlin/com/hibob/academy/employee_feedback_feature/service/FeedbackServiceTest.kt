package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Response
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
    private val requestContext = mock<ContainerRequestContext>()

    @Test
    fun `submitFeedback should submit feedback and return its ID`() {
        val feedbackId = 1L
        val feedbackRequest = FeedbackRequest(feedbackText, isAnonymous, department)

        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("employeeId")).thenReturn(employeeId)
        whenever(feedbackDao.submitFeedback(FeedbackSubmission(employeeId, companyId, feedbackRequest.feedbackText, feedbackRequest.isAnonymous, feedbackRequest.department)))
            .thenReturn(feedbackId)

        val response = feedbackService.submitFeedback(requestContext, feedbackRequest)

        assertEquals(Response.Status.OK.statusCode, response.status)
    }

    @Test
    fun `getFeedbackHistory should return feedback history when valid`() {
        val feedbackDataList = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)
        )

        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("employeeId")).thenReturn(employeeId)
        whenever(feedbackDao.getFeedbackHistory(companyId, employeeId)).thenReturn(feedbackDataList)

        val result = feedbackService.getFeedbackHistory(requestContext)

        assertEquals(Response.Status.OK.statusCode, result.status)
        assertEquals(feedbackDataList, result.entity)
    }

    @Test
    fun `getFeedbackHistory should return 400 when companyId is missing`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(null)
        whenever(requestContext.getProperty("employeeId")).thenReturn(employeeId)

        val result = feedbackService.getFeedbackHistory(requestContext)

        assertEquals(Response.Status.BAD_REQUEST.statusCode, result.status)
        assertEquals("Bad Request: Missing companyId", result.entity)
    }

    @Test
    fun `getFeedbackHistory should return 400 when employeeId is missing`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("employeeId")).thenReturn(null)

        val result = feedbackService.getFeedbackHistory(requestContext)

        assertEquals(Response.Status.BAD_REQUEST.statusCode, result.status)
        assertEquals("Bad Request: Missing employeeId", result.entity)
    }

    @Test
    fun `getAllFeedbacks should return all feedbacks when authorized`() {
        val feedbackDataList = listOf(
            FeedbackData(1L, employeeId, companyId, feedbackText, isAnonymous, department, LocalDateTime.now(), true)
        )

        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("role")).thenReturn(EmployeeRole.ADMIN.name)
        whenever(feedbackDao.getAllFeedbacks(companyId)).thenReturn(feedbackDataList)

        val result = feedbackService.getAllFeedbacks(requestContext)

        assertEquals(Response.Status.OK.statusCode, result.status)
        assertEquals(feedbackDataList, result.entity)
    }

    @Test
    fun `getAllFeedbacks should return 400 when role is missing`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("role")).thenReturn(null)

        val result = feedbackService.getAllFeedbacks(requestContext)

        assertEquals(Response.Status.BAD_REQUEST.statusCode, result.status)
        assertEquals("Bad Request: Missing role", result.entity)
    }

    @Test
    fun `getAllFeedbacks should return 400 when role is invalid`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("role")).thenReturn("INVALID_ROLE")

        val result = feedbackService.getAllFeedbacks(requestContext)

        assertEquals(Response.Status.BAD_REQUEST.statusCode, result.status)
        assertEquals("Bad Request: Role not found!", result.entity)
    }

    @Test
    fun `getAllFeedbacks should return 403 when employee is not authorized`() {
        whenever(requestContext.getProperty("companyId")).thenReturn(companyId)
        whenever(requestContext.getProperty("role")).thenReturn(EmployeeRole.EMPLOYEE.name)

        val result = feedbackService.getAllFeedbacks(requestContext)

        assertEquals(Response.Status.FORBIDDEN.statusCode, result.status)
        assertEquals("Forbidden: You do not have access to view feedbacks", result.entity)
    }
}