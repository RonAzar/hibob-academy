package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Component

@Component
class FeedbackService @Autowired constructor(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(newFeedback: FeedbackSubmission): Long {
        return feedbackDao.submitFeedback(newFeedback)
    }

    fun getFeedbackHistory(@Context requestContext: ContainerRequestContext): Response {
        val companyId = requestContext.getProperty("companyId") as? Long
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()

        val employeeId = requestContext.getProperty("employeeId") as? Long
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing employeeId").build()

        val feedbackHistory = feedbackDao.getFeedbackHistory(companyId, employeeId)
        return Response.ok(feedbackHistory).build()
    }

    fun getAllFeedbacks(@Context requestContext: ContainerRequestContext): Response {
        val companyId = requestContext.getProperty("companyId") as? Long
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()

        val roleString = requestContext.getProperty("role") as? String
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing role").build()

        val employeeRole = try {
            EmployeeRole.valueOf(roleString.uppercase())
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Role not found!").build()
        }

        if (employeeRole == EmployeeRole.ADMIN || employeeRole == EmployeeRole.HR) {
            return Response.ok(feedbackDao.getAllFeedbacks(companyId)).build()
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Forbidden: You do not have access to view feedbacks").build()
    }
}