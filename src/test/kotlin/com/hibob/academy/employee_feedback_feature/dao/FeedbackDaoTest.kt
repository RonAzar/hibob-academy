package com.hibob.academy.employee_feedback_feature.dao


import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {
    private val companyId = 3L
    private val employeeId = 1L
    private val feedbackText = "What a feedback!"
    private val isAnonymous = false
    private val department = "Sales"
    private val testFeedback = FeedbackSubmission(employeeId, companyId, feedbackText, isAnonymous, department)
    private val dao = FeedbackDao(sql)
    val feedback = FeedbackTable.instance

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedback).where(feedback.companyId.eq(companyId)).execute()
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

        val feedbackInDB = dao.getFeedbackByFeedbackId(companyId,submittedFeedbackId)

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
        assertEquals(department, retrievedFeedback.department)
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