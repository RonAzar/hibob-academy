package com.hibob.academy.rest_api

import jakarta.ws.rs.NotFoundException
import org.springframework.stereotype.Service

@Service
class OwnerServices {
    data class Owner(
        val ownerId: Long,
        val name: String?,
        val firstName: String?,
        val lastName: String?,
        val companyId: Long,
        val employeeId: Long
    )

    private val owners = mutableListOf(
        Owner(1L, null, "Ron", "Azar", 101L, 10L),
        Owner(2L, null, "Or", "Azar", 102L, 11L),
        Owner(3L, "Noam", null, null, 103L, 12L)
    )

    fun validateOwner(owner: Owner) {
        ValidationUtils.validateNameFields(owner.name, owner.firstName, owner.lastName)
    }

    fun getAllOwners(): List<Owner> {
        return owners
    }

    fun getOwnerById(ownerId: Long): Owner {
        return owners.find { it.ownerId == ownerId }
            ?: throw NotFoundException("No owner with id $ownerId")
    }

    fun addOwner(newOwner: Owner): Owner {
        validateOwner(newOwner)
        owners.add(newOwner)
        return newOwner
    }

    fun updateOwner(ownerId: Long, updatedOwner: Owner): Owner {
        validateOwner(updatedOwner)
        val index = owners.indexOfFirst { it.ownerId == ownerId }
        if (index >= 0) {
            owners[index] = updatedOwner.copy(ownerId = ownerId)
            return updatedOwner
        } else {
            throw NotFoundException("No owner with id $ownerId")
        }
    }

    fun deleteOwner(ownerId: Long): Owner {
        val owner = owners.find { it.ownerId == ownerId }
            ?: throw NotFoundException("No owner with id $ownerId")
        owners.remove(owner)
        return owner
    }
}