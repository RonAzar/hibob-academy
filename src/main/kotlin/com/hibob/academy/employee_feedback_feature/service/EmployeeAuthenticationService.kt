package com.hibob.academy.employee_feedback_feature.service

import com.hibob.academy.dao.PetDao
import com.hibob.academy.employee_feedback_feature.dao.EmployeeDao
import com.hibob.academy.employee_feedback_feature.dao.EmployeeLogin
import com.hibob.academy.service.SessionService
import com.hibob.academy.service.SessionService.Companion
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmployeeAuthenticationService @Autowired constructor(private val employeeDao: EmployeeDao) {
    companion object{
        const val SECRET_KEY = "L$5t!6JwZ^qH@1n#Gv3xT*%u9Rp8*E&yK7f^c!Q2(zV1lP*m#7BxW5hQ2dN&G$2w8m!K6#E3jR#qP1sV%%gY^nU5c^Z4t!"
    }

    fun createJwtTokenForEmployee(employeeLogin: EmployeeLogin): String {
        val authenticatedEmployee = employeeDao.authenticateEmployee(employeeLogin)
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim("employeeId", authenticatedEmployee.employeeId)
            .claim("companyId", authenticatedEmployee.companyId)
            .claim("role", authenticatedEmployee.role)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS512, SessionService.SECRET_KEY)
            .compact()
    }
}