package com.hibob.academy.employee_feedback_feature.validation

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.core.Response

class RolePermissionValidator {
    companion object {
        fun extractClaimAsLong(requestContext: ContainerRequestContext, claim: String): Long? {
            return (requestContext.getProperty(claim) as? Number)?.toLong() ?: run {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("UNAUTHORIZED: Missing or invalid $claim").build())
                null
            }
        }

        fun extractRole(requestContext: ContainerRequestContext): EmployeeRole? {
            return (requestContext.getProperty("role") as? String)?.uppercase()?.let {
                try {
                    EmployeeRole.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("UNAUTHORIZED: Invalid role").build())
                    null
                }
            } ?: run {
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity("UNAUTHORIZED: Missing role").build()
                )
                null
            }
        }

        fun hrOrAdminValidation(role: EmployeeRole): Boolean {
            return role == EmployeeRole.ADMIN || role == EmployeeRole.HR
        }

        fun changeFeedbackStatusPermission(role: EmployeeRole): Boolean {
            return role == EmployeeRole.HR
        }
    }
}