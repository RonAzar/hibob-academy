package com.hibob.academy.rest_api

import jakarta.ws.rs.BadRequestException

object ValidationUtils {

    fun validateNameFields(name: String?, firstName: String?, lastName: String?) {
        val hasName = name?.isNotBlank() == true
        val hasFirstName = firstName?.isNotBlank() == true
        val hasLastName = lastName?.isNotBlank() == true

        if (hasName && (hasFirstName || hasLastName)) {
            throw BadRequestException("Cannot have both a full name and first name/last name.")
        }

        if (!hasName && (!hasFirstName || !hasLastName)) {
            throw BadRequestException("Must have either a full name or both first name and last name.")
        }
    }
}