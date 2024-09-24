package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.employee_feedback_feature.dao.EmployeeDao
import com.hibob.academy.employee_feedback_feature.dao.EmployeeLogin
import com.hibob.academy.filters.AuthenticationFilter.Companion.COOKIE_NAME
import com.hibob.academy.filters.AuthenticationFilter.Companion.SECRET_KEY
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.ws.rs.core.NewCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmployeeAuthenticationService @Autowired constructor(private val employeeDao: EmployeeDao) {
    fun createJwtTokenForEmployee(employeeLogin: EmployeeLogin): String {
        val authenticatedEmployee = employeeDao.authenticateEmployee(employeeLogin)

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("employeeId", authenticatedEmployee.employeeId)
            .claim("companyId", authenticatedEmployee.companyId)
            .claim("role", authenticatedEmployee.role)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact()
    }

    fun createLogoutCookie(): NewCookie {
        return NewCookie(COOKIE_NAME, null, "/api/", null, "Logout Cookie", 0, false)
    }
}