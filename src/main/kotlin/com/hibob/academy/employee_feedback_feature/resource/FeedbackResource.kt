package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.FeedbackRequest
import com.hibob.academy.employee_feedback_feature.service.FeedbackService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
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
        return feedbackService.submitFeedback(requestContext, newFeedback)
    }

    @Path("/history")
    @GET
    fun getFeedbackHistory(@Context requestContext: ContainerRequestContext): Response {
        return feedbackService.getFeedbackHistory(requestContext)
    }
}