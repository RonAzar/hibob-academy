package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.FeedbackRequest
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import com.hibob.academy.employee_feedback_feature.service.FeedbackService
import jakarta.ws.rs.*
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.extractClaimAsLong
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.extractRole
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.hasPermission
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.Permissions

@Controller
@Path("/api/feedback")
class FeedbackResource(private val feedbackService: FeedbackService) {
    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitFeedback(@Context requestContext: ContainerRequestContext, newFeedback: FeedbackRequest): Response {
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val employeeId = if (!newFeedback.isAnonymous) {
            extractClaimAsLong(requestContext, "employeeId")!!
        } else {
            null
        }

        val feedbackSubmission = FeedbackSubmission(employeeId, companyId, newFeedback.feedbackText, newFeedback.isAnonymous, newFeedback.department)

        return Response.ok(feedbackService.submitFeedback(feedbackSubmission)).build()
    }

    @Path("/history")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getFeedbackHistory(@Context requestContext: ContainerRequestContext): Response {
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val employeeId = extractClaimAsLong(requestContext, "employeeId")!!

        return Response.ok(feedbackService.getFeedbackHistory(companyId, employeeId)).build()
    }

    @Path("/view/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllFeedbacks(@Context requestContext: ContainerRequestContext): Response {
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val employeeRole = extractRole(requestContext)!!

        return if (hasPermission(employeeRole, Permissions.VIEW_ALL_FEEDBACKS)) {
            Response.ok(feedbackService.getAllFeedbacks(companyId)).build()
        } else {
            Response.status(Response.Status.FORBIDDEN).entity("UNAUTHORIZED: You do not have access to view feedbacks").build()
        }
    }
}