package com.hibob.academy.employee_feedback_feature.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FeedbackDao @Autowired constructor(private val sql: DSLContext) {
    private val feedback = FeedbackTable.instance

    private val feedbackDataMapper = RecordMapper<Record, FeedbackData> { record ->
        FeedbackData(
            record[feedback.id],
            record[feedback.employeeId],
            record[feedback.companyId],
            record[feedback.feedbackText],
            record[feedback.isAnonymous],
            record[feedback.department],
            record[feedback.createdAt],
            record[feedback.status]
        )
    }

    fun submitFeedback(newFeedback: FeedbackSubmission): Long {
        val employeeId = if (newFeedback.isAnonymous) null else newFeedback.employeeId

        return sql.insertInto(feedback)
            .set(feedback.employeeId, employeeId)
            .set(feedback.companyId, newFeedback.companyId)
            .set(feedback.feedbackText, newFeedback.feedbackText)
            .set(feedback.isAnonymous, newFeedback.isAnonymous)
            .set(feedback.department, newFeedback.department)
            .returning(feedback.id)
            .fetchOne()!!
            .get(feedback.id)
    }

    fun getFeedbackByFeedbackId(companyId: Long, feedbackId: Long): FeedbackData? {
        return selectFeedback()
            .where(feedback.id.eq(feedbackId))
            .and(feedback.companyId.eq(companyId))
            .fetchOne(feedbackDataMapper)
    }

    fun getFeedbackHistory(companyId: Long, employeeId: Long): List<FeedbackData> {
        return selectFeedback()
            .where(feedback.companyId.eq(companyId))
            .and(feedback.employeeId.eq(employeeId))
            .fetch(feedbackDataMapper)
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        return selectFeedback()
            .where(feedback.companyId.eq(companyId))
            .fetch(feedbackDataMapper)
    }

    private fun selectFeedback() = sql.select(feedback.id, feedback.companyId, feedback.employeeId, feedback.feedbackText, feedback.isAnonymous, feedback.department, feedback.createdAt, feedback.status)
        .from(feedback)
}