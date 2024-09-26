package com.hibob.academy.employee_feedback_feature.dao

import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class FeedbackResponseDaoTest @Autowired constructor(private val sql: DSLContext){
    private val responseText = "What a Response!"
    private val responderId = -1L
    private val feedbackId = -2L
    private val testResponse = ResponseSubmission(responseText, feedbackId)
    private val dao = FeedbackResponseDao(sql)
    val response = FeedbackResponseTable.instance

    @AfterEach
    fun cleanup() {
        sql.deleteFrom(response).where(response.feedbackId.eq(feedbackId)).execute()
    }

    @Test
    fun `Submit new response`() {
        val submittedResponseId = dao.submitResponse(testResponse, responderId)
        assertNotNull(submittedResponseId)
        assertNotNull(dao.getResponseById(submittedResponseId))
    }
}