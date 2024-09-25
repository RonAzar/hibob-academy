package com.hibob.academy.employee_feedback_feature.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FeedbackResponseDao @Autowired constructor(private val sql: DSLContext) {
    private val response = FeedbackResponseTable.instance

    private val feedbackResponseDataMapper = RecordMapper<Record, FeedbackResponseData> { record ->
        FeedbackResponseData(
            record[response.id],
            record[response.responseText],
            record[response.responderId],
            record[response.feedbackId],
            record[response.createdAt]
            )
    }

    fun submitResponse(newFeedbackResponse: ResponseSubmission): Long {
        return sql.insertInto(response)
            .set(response.responseText, newFeedbackResponse.responseText)
            .set(response.responderId, newFeedbackResponse.responderId)
            .set(response.feedbackId, newFeedbackResponse.feedbackId)
            .returning(response.id)
            .fetchOne()!!
            .get(response.id)
    }

    fun getResponseById(id: Long): FeedbackResponseData? {
        return sql.select(response.id, response.responseText, response.responderId, response.feedbackId, response.createdAt)
            .from(response)
            .where(response.id.eq(id))
            .fetchOne(feedbackResponseDataMapper)
    }
}