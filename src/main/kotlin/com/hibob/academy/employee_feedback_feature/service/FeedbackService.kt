package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.EmployeeRole
import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackSubmission
import com.hibob.academy.filters.AuthenticationFilter.Companion.COOKIE_NAME
import com.hibob.academy.filters.AuthenticationFilter.Companion.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.Response
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Component

@Component
class FeedbackService @Autowired constructor(private val feedbackDao: FeedbackDao) {
    fun submitFeedback(newFeedback: FeedbackSubmission): Long {
        return feedbackDao.submitFeedback(newFeedback)
    }

    fun getFeedbackHistory(@Context headers: HttpHeaders): Response {
        val claimsResult = extractClaimsFromCookie(headers)
        if (claimsResult.status != Response.Status.OK.statusCode) {
            return claimsResult
        }
        val claims = claimsResult.entity as Claims

        val companyId = claims["companyId"] as? Long ?: return Response.status(Response.Status.BAD_REQUEST)
            .entity("Bad Request: Missing companyId").build()
        val employeeId = claims["employeeId"] as? Long ?: return Response.status(Response.Status.BAD_REQUEST)
            .entity("Bad Request: Missing employeeId").build()

        val feedbackHistory = feedbackDao.getFeedbackHistory(companyId, employeeId)
        return Response.ok(feedbackHistory).build()
    }

    fun getAllFeedbacks(@Context headers: HttpHeaders): Response {
        val claimsResult = extractClaimsFromCookie(headers)
        if (claimsResult.status != Response.Status.OK.statusCode) {
            return claimsResult
        }
        val claims = claimsResult.entity as Claims

        val companyId = claims["companyId"] as? Long ?: return Response.status(Response.Status.BAD_REQUEST)
            .entity("Bad Request: Missing companyId").build()
        val employeeRole = try {
            EmployeeRole.valueOf(claims["role"].toString().uppercase())
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: role not found!").build()
        }

        if (employeeRole == EmployeeRole.ADMIN || employeeRole == EmployeeRole.HR) {
            return Response.ok(feedbackDao.getAllFeedbacks(companyId)).build()
        }
        return Response.status(Response.Status.FORBIDDEN).entity("Forbidden: You do not have access to view feedbacks").build()
    }

    // Private method to handle JWT extraction and validation
    private fun extractClaimsFromCookie(headers: HttpHeaders): Response {
        val cookie: Cookie = headers.cookies[COOKIE_NAME]
            ?: return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: No session cookie found").build()

        val jwtToken = cookie.value
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwtToken)
                .body
            Response.ok(claims).build()
        } catch (e: Exception) {
            Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Invalid token").build()
        }
    }
}