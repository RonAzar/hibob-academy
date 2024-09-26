package com.hibob.academy.employee_feedback_feature.validation

import jakarta.ws.rs.BadRequestException

class ValidateSubmission {
    companion object{
        fun validateTextSubmission(submittedString: String, maxLength: Long): Boolean {
            if (submittedString.length > maxLength) {
                throw BadRequestException("Error: Input too long!")
            }
            if (submittedString.isEmpty()) {
                throw BadRequestException("Error: Empty Submission!")
            }
            return true
        }
    }
}