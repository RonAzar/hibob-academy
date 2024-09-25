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
        enum class Permissions {
            VIEW_ALL_FEEDBACKS,
            CHANGE_FEEDBACK_STATUS,
        }


        private val rolePermissions = mapOf(
            EmployeeRole.HR to setOf(Permissions.VIEW_ALL_FEEDBACKS, Permissions.CHANGE_FEEDBACK_STATUS),
            EmployeeRole.ADMIN to setOf(Permissions.VIEW_ALL_FEEDBACKS),
        )

        fun hasPermission(role: EmployeeRole, permission: Permissions): Boolean {
            return rolePermissions[role]?.contains(permission) ?: false
        }
    }
}