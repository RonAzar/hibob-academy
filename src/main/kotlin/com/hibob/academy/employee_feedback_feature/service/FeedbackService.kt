package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.FeedbackDao
import com.hibob.academy.employee_feedback_feature.dao.FeedbackData
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
        // Extract the session cookie
        val cookie: Cookie? = headers.cookies[COOKIE_NAME]

        val jwtToken = cookie!!.value

        // Decode JWT to extract companyId and employeeId
        val claims: Claims
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)  // Replace with your actual secret key
                .build()
                .parseClaimsJws(jwtToken)
                .body
        } catch (e: Exception) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Invalid token").build()
        }

        // Extracting companyId and employeeId from the JWT claims
        val companyId = claims["companyId"] as? Long ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing companyId").build()
        val employeeId = claims["employeeId"] as? Long ?: return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request: Missing employeeId").build()

//        // Check if the employee exists in the database - if not, return 404 Not Found
//        val employeeExists = employeeDao.checkEmployeeExists(companyId, employeeId)
//        if (!employeeExists) {
//            return Response.status(Response.Status.NOT_FOUND).entity("Not Found: Employee does not exist").build()
//        }
//
//        // Check if the employee is authenticated as admin/hr/current employee - if not, return 401 Unauthorized
//        val isAuthenticated = employeeDao.checkAuthentication(companyId, employeeId)
//        if (!isAuthenticated) {
//            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized: Access denied").build()
//        }

        // Fetch feedback history
        val feedbackHistory = feedbackDao.getFeedbackHistory(companyId, employeeId)
        return Response.ok(feedbackHistory).build()
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        //Need to check if employee authenticate admin/hr - if not--> 401 Unauthorized
        return feedbackDao.getAllFeedbacks(companyId)
    }
}