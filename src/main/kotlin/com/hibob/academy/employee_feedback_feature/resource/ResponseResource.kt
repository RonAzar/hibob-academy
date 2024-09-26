package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import com.hibob.academy.employee_feedback_feature.dao.ResponseSubmission
import com.hibob.academy.employee_feedback_feature.service.ResponseService
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.extractClaimAsLong
import com.hibob.academy.employee_feedback_feature.validation.RolePermissionValidator.Companion.validatePermissions
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Path("/api/response")
class ResponseResource(private val responseService: ResponseService) {
    @Path("/submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun submitResponse(@Context requestContext: ContainerRequestContext, newFeedbackResponse: ResponseSubmission): Response {
        val companyId = validatePermissions(
            requestContext, setOf(EmployeeRole.HR),
            "UNAUTHORIZED: You do not have permission to respond to feedbacks"
        )
        val employeeId = extractClaimAsLong(requestContext, "employeeId")

        val responseId = responseService.submitResponse(newFeedbackResponse, employeeId, companyId)

        return Response.ok("Response submitted successfully, response id: $responseId").build()
    }
}