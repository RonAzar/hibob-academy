package com.hibob.academy.employee_feedback_feature.resource

import com.hibob.academy.employee_feedback_feature.dao.EmployeeLogin
import com.hibob.academy.employee_feedback_feature.service.EmployeeAuthenticationService
import com.hibob.academy.filters.AuthenticationFilter.Companion.COOKIE_NAME
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

@Controller
@Produces
@Path("/api/auth/employee")
class EmployeeAuthResource(private val employeeService: EmployeeAuthenticationService) {
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun login(employeeLogin: EmployeeLogin): Response {
        val jwtToken = employeeService.createJwtTokenForEmployee(employeeLogin)
        val cookie = NewCookie.Builder(COOKIE_NAME).value(jwtToken).path("/api/").build()
        return Response.ok().cookie(cookie).build()
    }

    @Path("/logout")
    @GET
    fun logout(): Response {
        val expiredCookie = employeeService.createLogoutCookie()
        return Response.ok("Logged out successfully").cookie(expiredCookie).build()
    }
}