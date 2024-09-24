package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import com.hibob.academy.employee_feedback_feature.dao.FeedbackRequest
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import com.hibob.academy.employee_feedback_feature.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/feedback")
class FeedbackResource(private val feedbackService: FeedbackService) {
    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitFeedback(@Context requestContext: ContainerRequestContext, newFeedback: FeedbackRequest): Response {
        val companyId = requestContext.getProperty("companyId") as? Number
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()

        val employeeId = if (!newFeedback.isAnonymous) {
            requestContext.getProperty("employeeId") as? Number
                ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing employeeId").build()
        } else null

        val feedbackSubmission = FeedbackSubmission(employeeId?.toLong(), companyId.toLong(), newFeedback.feedbackText, newFeedback.isAnonymous, newFeedback.department)

        return Response.ok(feedbackService.submitFeedback(requestContext, newFeedback)).build()
    }

    @Path("/history")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getFeedbackHistory(@Context requestContext: ContainerRequestContext): Response {
        val companyId = requestContext.getProperty("companyId") as? Number
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()

        val employeeId = requestContext.getProperty("employeeId") as? Number
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing employeeId").build()

        return Response.ok(feedbackService.getFeedbackHistory(requestContext)).build()
    }

    @Path("/view/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllFeedbacks(@Context requestContext: ContainerRequestContext): Response {
        val companyId = requestContext.getProperty("companyId") as? Number
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()

        val roleString = requestContext.getProperty("role") as? String
            ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing role").build()

        val employeeRole = try {
            EmployeeRole.valueOf(roleString.uppercase())
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Role not found!").build()
        }

        if (employeeRole == EmployeeRole.ADMIN || employeeRole == EmployeeRole.HR) {
            return Response.ok(feedbackService.getAllFeedbacks(requestContext)).build()
        }

        return Response.status(Response.Status.FORBIDDEN).entity("Forbidden: You do not have access to view feedbacks").build()
    }
}