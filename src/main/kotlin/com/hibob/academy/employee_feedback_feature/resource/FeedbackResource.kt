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
            Response.status(Response.Status.UNAUTHORIZED)
                .entity("UNAUTHORIZED: You do not have access to view feedbacks")
                .build()
        }
    }

    @Path("view/filter")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun getFilteredFeedbacks(@Context requestContext: ContainerRequestContext, filter: FeedbackFilter): Response {
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val employeeRole = extractRole(requestContext)!!

        return if (hasPermission(employeeRole, Permissions.VIEW_ALL_FEEDBACKS)) {
            Response.ok(feedbackService.getFeedbacksUsingFilter(filter, companyId)).build()
        } else {
            Response.status(Response.Status.UNAUTHORIZED)
                .entity("UNAUTHORIZED: You do not have access to view feedbacks").build()
        }
    }

    @Path("view/status/{feedbackId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatus(
        @Context requestContext: ContainerRequestContext,
        @PathParam("feedbackId") feedbackId: Long
    ): Response {
        val role = extractRole(requestContext)!!
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val searchedFeedback = SearchedFeedback(companyId, feedbackId)

        if (!hasPermission(role, Permissions.VIEW_ALL_FEEDBACKS)) {

            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("UNAUTHORIZED: You do not have access to view feedback status!").build()
        }

        return Response.ok("feedback status: ${feedbackService.getFeedbackStatus(searchedFeedback)}").build()
    }

    @Path("update/status")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    fun updateStatus(@Context requestContext: ContainerRequestContext, updateFeedback: UpdateFeedbackStatus): Response {
        val companyId = extractClaimAsLong(requestContext, "companyId")!!
        val role = extractRole(requestContext)!!

        if (!hasPermission(role, Permissions.CHANGE_FEEDBACK_STATUS)) {

            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("UNAUTHORIZED: You do not have access to change feedback status!").build()
        }

        return Response.ok(feedbackService.updateFeedbackStatus(updateFeedback, companyId)).build()
    }
}