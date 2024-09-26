package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.*
import com.hibob.academy.employee_feedback_feature.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.extractClaimAsLong
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.validatePermissions

@Controller
@Path("/api/feedback")
class FeedbackResource(private val feedbackService: FeedbackService) {
    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitFeedback(@Context requestContext: ContainerRequestContext, newFeedback: FeedbackRequest): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.EMPLOYEE, EmployeeRole.ADMIN, EmployeeRole.HR, EmployeeRole.MANAGER),
            "UNAUTHORIZED: You do not have access to submit feedbacks"
        )

        val employeeId = if (!newFeedback.isAnonymous) {
            extractClaimAsLong(requestContext, "employeeId")
        } else {
            null
        }

        val feedbackSubmission = FeedbackSubmission(
            employeeId,
            companyId,
            newFeedback.feedbackText,
            newFeedback.isAnonymous,
            newFeedback.department
        )

        return Response.ok(feedbackService.submitFeedback(feedbackSubmission)).build()
    }

    @Path("/history")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getFeedbackHistory(@Context requestContext: ContainerRequestContext): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.EMPLOYEE, EmployeeRole.ADMIN, EmployeeRole.HR, EmployeeRole.MANAGER),
            "UNAUTHORIZED: You do not have access to get history"
        )
        val employeeId = extractClaimAsLong(requestContext, "employeeId")

        return Response.ok(feedbackService.getFeedbackHistory(companyId, employeeId)).build()
    }

    @Path("/view/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllFeedbacks(@Context requestContext: ContainerRequestContext): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.HR, EmployeeRole.ADMIN),
            "UNAUTHORIZED: You do not have access to view feedbacks"
        )

        return Response.ok(feedbackService.getAllFeedbacks(companyId)).build()
    }

    @Path("view/filter")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun getFilteredFeedbacks(@Context requestContext: ContainerRequestContext, filter: FeedbackFilter): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.HR, EmployeeRole.ADMIN),
            "UNAUTHORIZED: You do not have access to view feedbacks"
        )

        return Response.ok(feedbackService.getFeedbacksUsingFilter(filter, companyId)).build()
    }

    @Path("view/status/{feedbackId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatus(
        @Context requestContext: ContainerRequestContext,
        @PathParam("feedbackId") feedbackId: Long
    ): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.HR, EmployeeRole.ADMIN),
            "UNAUTHORIZED: You do not have access to view feedback status!"
        )
        val searchedFeedback = SearchedFeedback(companyId, feedbackId)

        return Response.ok("feedback status: ${feedbackService.getFeedbackStatus(searchedFeedback)}").build()
    }

    @Path("update/status")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    fun updateStatus(@Context requestContext: ContainerRequestContext, updateFeedback: UpdateFeedbackStatus): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.HR),
            "UNAUTHORIZED: You do not have access to change feedback status!"
        )

        return Response.ok(feedbackService.updateFeedbackStatus(updateFeedback, companyId)).build()
    }
}