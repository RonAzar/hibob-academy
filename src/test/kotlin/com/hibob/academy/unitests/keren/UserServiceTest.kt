package com.hibob.academy.unitests.keren

import com.hibob.bootcamp.unittests.*
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class UserServiceTest{
    val token = "Waffle"
    val id: Long= 123

    private val user = User(123,"Ron Azar", "ron.azar@hibob.io" ,"12345", true)
    private val userDao=  mock<UserDao>{}
    private val notificationService=  mock<NotificationService>{}
    private val emailVerificationService= mock<EmailVerificationService>{}
    private val userService=UserService(userDao,notificationService,emailVerificationService)

    @Test
    fun `Test register user findById -- User already exists`(){
        whenever( userDao.findById(user.id)).thenReturn(user)
        val errorMessage = assertThrows<IllegalArgumentException>{
            userService.registerUser(user)
        }
        assertEquals("User already exists",errorMessage.message)

        // Verifying that save and sendVerificationEmail are NOT called because the user exists
        verify(userDao, never()).save(any())
        verify(emailVerificationService, never()).sendVerificationEmail(any())
    }

    @Test
    fun `Test register user findByEmail -- User registration failed`(){
        whenever( userDao.findById(user.id)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(false)
        val errorMessage = assertThrows<IllegalStateException>{
            userService.registerUser(user)
        }
        assertEquals("User registration failed",errorMessage.message)

        verify(emailVerificationService, never()).sendVerificationEmail(any())
    }

    @Test
    fun `Test register user findByEmail -- Failed to send verification email`(){
        whenever( userDao.findById(user.id)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(false)
        val errorMessage = assertThrows<IllegalStateException>{
            userService.registerUser(user)
        }
        assertEquals("Failed to send verification email",errorMessage.message)
    }

    @Test
    fun `Test register user findByEmail -- Successfully registered user`(){
        whenever( userDao.findById(user.id)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(true)
        assertTrue(userService.registerUser(user),"User not added")
    }

    @Test
    fun `verify User Email -- User not found`(){
        whenever( userDao.findById(id)).thenReturn(null)
        val errorMessage = assertThrows<IllegalArgumentException>{
            userService.verifyUserEmail(id,token)
        }
        assertEquals("User not found",errorMessage.message)
        verify(emailVerificationService, never()).verifyEmail(any(), any())
        verify(userDao, never()).save(any())
        verify(userDao, never()).update(any())
    }

    @Test
    fun `Test verify User Email -- Email verification failed`(){
        whenever( userDao.findById(id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(false)
        val errorMessage = assertThrows<IllegalArgumentException>{
            userService.verifyUserEmail(id,token)
        }
        assertEquals("Email verification failed",errorMessage.message)
        verify(userDao, never()).save(any())
        verify(userDao, never()).update(any())
    }

    @Test
    fun `Test verify User Email -- isUpdated-true- send email`(){
        whenever( userDao.findById(id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(true)
        val updatedUser = user.copy()
        whenever(userDao.update(updatedUser)).thenReturn(true)
        assertTrue(userService.verifyUserEmail(id,token),"User updated")
    }

    @Test
    fun `Test verify User Email -- isUpdated-false- dont send email`(){
        whenever( userDao.findById(id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(true)
        val updatedUser = user.copy()
        whenever(userDao.update(updatedUser)).thenReturn(false)
        assertFalse(userService.verifyUserEmail(id,token),"User updated")
    }
}