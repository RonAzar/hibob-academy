package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val companyId = 3L
    private val employeeId = 1L
    private val feedbackText = "What a feedback!"
    private val isAnonymous = false
    private val departmentSales = "Sales"
    private val departmentHR = "HR"
    private val createdAt = LocalDateTime.now().minusDays(3)
    private val testFeedback = FeedbackSubmission(employeeId, companyId, feedbackText, isAnonymous, departmentSales)
    private val testFeedbackSales = FeedbackSubmission(employeeId, companyId, feedbackText, false, departmentSales)
    private val testFeedbackHR = FeedbackSubmission(employeeId, companyId, feedbackText, true, departmentHR)
    private val dao = FeedbackDao(sql)
    val feedback = FeedbackTable.instance

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedback).where(feedback.companyId.eq(companyId)).execute()
    }

    @Test
    fun `Get feedbacks using filter - filter by isAnonymous`() {
        dao.submitFeedback(testFeedbackSales)
        dao.submitFeedback(testFeedbackHR)

        val filter = FeedbackFilter(isAnonymous = true, createdAt = null, department = null)
        val feedbacks = dao.getFeedbacksUsingFilter(filter, companyId)

        assertNotNull(feedbacks)
        assertEquals(1, feedbacks.size)
        assertTrue(feedbacks[0].isAnonymous, "Expected feedback to be anonymous")
    }

    @Test
    fun `Get feedbacks using filter - filter by department`() {
        dao.submitFeedback(testFeedbackSales)
        dao.submitFeedback(testFeedbackHR)

        val filter = FeedbackFilter(isAnonymous = null, createdAt = null, department = departmentSales)
        val feedbacks = dao.getFeedbacksUsingFilter(filter, companyId)

        assertNotNull(feedbacks)
        assertEquals(1, feedbacks.size)
        assertEquals(departmentSales, feedbacks[0].department)
    }

    @Test
    fun `Get feedbacks using filter - filter by createdAt`() {
        dao.submitFeedback(testFeedbackSales)
        dao.submitFeedback(testFeedbackHR)

        val filter =
            FeedbackFilter(createdAt = createdAt.minusDays(1), department = null, isAnonymous = null)
        val feedbacks = dao.getFeedbacksUsingFilter(filter, companyId)

        assertNotNull(feedbacks)
        assertEquals(2, feedbacks.size)
    }

    @Test
    fun `Get feedback status - feedback exists`() {
        val submittedFeedbackId = dao.submitFeedback(testFeedback)

        val searchedFeedback = SearchedFeedback(companyId, submittedFeedbackId)
        val feedbackStatus = dao.getFeedbackStatus(searchedFeedback)

        assertNotNull(feedbackStatus)
        assertTrue(feedbackStatus.status)
        assertEquals(feedbackText, feedbackStatus.feedbackText)
    }

    @Test
    fun `Get feedback status - feedback does not exist`() {
        val searchedFeedback = SearchedFeedback(companyId, -1L)

        val exception = assertThrows<IllegalArgumentException> {
            dao.getFeedbackStatus(searchedFeedback)
        }
        assertEquals("Feedback not found: Incorrect feedbackId provided", exception.message)
    }

    @Test
    fun `Update feedback status - success`() {
        val submittedFeedbackId = dao.submitFeedback(testFeedback)

        val updateFeedback = UpdateFeedbackStatus(submittedFeedbackId, status = false)
        val rowsAffected = dao.updateFeedbackStatus(updateFeedback, companyId)

        assertEquals(1, rowsAffected)

        val updatedFeedback = dao.getFeedbackByFeedbackId(companyId, submittedFeedbackId)
        assertFalse(updatedFeedback?.status ?: true, "Expected feedback status to be updated to true")
    }

    @Test
    fun `Update feedback status - feedback does not exist`() {
        val updateFeedback = UpdateFeedbackStatus(-1L, status = true)
        val rowsAffected = dao.updateFeedbackStatus(updateFeedback, companyId)

        assertEquals(0, rowsAffected)
    }

    @Test
    fun `Submit new feedback- not anonymous`() {
        val submittedFeedbackId = dao.submitFeedback(testFeedback)
        assertNotNull(submittedFeedbackId)
    }

    @Test
    fun `Submit new feedback - anonymous submission`() {
        val anonymousFeedback = testFeedback.copy(isAnonymous = true)
        val submittedFeedbackId = dao.submitFeedback(anonymousFeedback)
        assertNotNull(submittedFeedbackId)

        val feedbackInDB = dao.getFeedbackByFeedbackId(companyId, submittedFeedbackId)

        assertNotNull(feedbackInDB)
        assertNull(feedbackInDB?.employeeId, "Expected employeeId to be null for anonymous feedback")
        assertEquals(true, feedbackInDB?.isAnonymous, "Expected feedback to be marked as anonymous")
    }

    @Test
    fun `Get feedback history - feedback exists`() {
        dao.submitFeedback(testFeedback)

        val feedbackHistory = dao.getFeedbackHistory(companyId, employeeId)

        assertNotNull(feedbackHistory)
        assertEquals(1, feedbackHistory.size)

        val retrievedFeedback = feedbackHistory.first()
        assertEquals(companyId, retrievedFeedback.companyId)
        assertEquals(employeeId, retrievedFeedback.employeeId)
        assertEquals(feedbackText, retrievedFeedback.feedbackText)
        assertEquals(isAnonymous, retrievedFeedback.isAnonymous)
        assertEquals(departmentSales, retrievedFeedback.department)
    }

    @Test
    fun `Get feedback history - multiple feedbacks for the same employee`() {
        dao.submitFeedback(testFeedback)
        val secondFeedback = testFeedback.copy(feedbackText = "Another feedback!")
        dao.submitFeedback(secondFeedback)

        val feedbackHistory = dao.getFeedbackHistory(companyId, employeeId)

        assertNotNull(feedbackHistory)
        assertEquals(2, feedbackHistory.size)

        val firstFeedback = feedbackHistory[0]
        val secondRetrievedFeedback = feedbackHistory[1]

        assertEquals(companyId, firstFeedback.companyId)
        assertEquals(employeeId, firstFeedback.employeeId)
        assertEquals(feedbackText, firstFeedback.feedbackText)

        assertEquals(companyId, secondRetrievedFeedback.companyId)
        assertEquals(employeeId, secondRetrievedFeedback.employeeId)
        assertEquals("Another feedback!", secondRetrievedFeedback.feedbackText)
    }

    @Test
    fun `Get all feedbacks - feedbacks exist for company`() {
        dao.submitFeedback(testFeedback)
        val secondFeedback = testFeedback.copy(feedbackText = "Another company feedback!")
        dao.submitFeedback(secondFeedback)

        val allFeedbacks = dao.getAllFeedbacks(companyId)

        assertNotNull(allFeedbacks)
        assertEquals(2, allFeedbacks.size)

        val firstFeedback = allFeedbacks[0]
        val secondFeedbackRetrieved = allFeedbacks[1]

        assertEquals(companyId, firstFeedback.companyId)
        assertEquals(companyId, secondFeedbackRetrieved.companyId)
    }

    @Test
    fun `Get all feedbacks - no feedbacks exist for company`() {
        val allFeedbacks = dao.getAllFeedbacks(companyId)

        assertNotNull(allFeedbacks)
        assertTrue(allFeedbacks.isEmpty())
    }

    @Test
    fun `Get all feedbacks - multiple feedbacks for the same company`() {
        dao.submitFeedback(testFeedback)
        val secondFeedback = testFeedback.copy(feedbackText = "Another feedback for same company")
        dao.submitFeedback(secondFeedback)

        val allFeedbacks = dao.getAllFeedbacks(companyId)

        assertNotNull(allFeedbacks)
        assertEquals(2, allFeedbacks.size)

        assertEquals(feedbackText, allFeedbacks[0].feedbackText)
        assertEquals("Another feedback for same company", allFeedbacks[1].feedbackText)
    }
}