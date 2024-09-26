package com.hibob.academy.employee_feedback_feature.validation

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.container.ContainerRequestContext

class RolePermissionValidator {
    companion object {
        fun extractClaimAsLong(requestContext: ContainerRequestContext, claim: String): Long {
            return (requestContext.getProperty(claim) as? Number)?.toLong() ?: run {

                throw BadRequestException("UNAUTHORIZED: Missing or invalid $claim")
            }
        }

        private fun extractRole(requestContext: ContainerRequestContext): EmployeeRole {
            return (requestContext.getProperty("role") as? String)?.uppercase()?.let {
                try {
                    EmployeeRole.valueOf(it)
                } catch (e: IllegalArgumentException) {

                    throw BadRequestException("UNAUTHORIZED: Invalid role")
                }
            } ?: run {

                throw BadRequestException("UNAUTHORIZED: Missing role")
            }
        }

        fun validatePermissions(
            requestContext: ContainerRequestContext,
            requiredRoles: Set<EmployeeRole>,
            errorMessage: String
        ): Long {
            val companyId = extractClaimAsLong(requestContext, "companyId")
            val role = extractRole(requestContext)

            if (!hasPermission(role, requiredRoles)) {
                throw BadRequestException(errorMessage)
            }

            return companyId
        }

        private fun hasPermission(role: EmployeeRole, requiredRoles: Set<EmployeeRole>): Boolean {
            return requiredRoles.contains(role)
        }
    }
}