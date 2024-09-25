package com.hibob.academy.employee_feedback_feature.dao

import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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

    fun getFeedbacksUsingFilter(filter: FeedbackFilter): List<FeedbackData> {
        var allFeedbacksByCompanyId = selectFeedbacksByCompanyId(filter.companyId)

        filter.isAnonymous?.let {
            allFeedbacksByCompanyId = allFeedbacksByCompanyId.and(feedback.isAnonymous.eq(it))
        }

        filter.department?.let {
            allFeedbacksByCompanyId = allFeedbacksByCompanyId.and(feedback.department.eq(it))
        }

        filter.createdAt?.let {
            allFeedbacksByCompanyId = allFeedbacksByCompanyId.and(feedback.createdAt.between(it, LocalDateTime.now()))
        }

        return allFeedbacksByCompanyId.fetch(feedbackDataMapper)
    }

    fun getFeedbackStatus(searchedFeedback: SearchedFeedback): FeedbackData{
        return selectFeedbacksByCompanyId(searchedFeedback.companyId)
            .and(feedback.id.eq(searchedFeedback.feedbackId))
            .fetchOne(feedbackDataMapper)?: throw IllegalArgumentException("Feedback not found: Incorrect feedbackId provided")
    }

    fun updateFeedbackStatus(updateFeedback: UpdateFeedbackStatus): Int{
        val rowsAffected = sql.update(feedback)
            .set(feedback.status, updateFeedback.status)
            .where(feedback.id.eq(updateFeedback.feedbackId))
            .and(feedback.companyId.eq(updateFeedback.companyId))
            .execute()

        return rowsAffected
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
        return selectFeedbacksByCompanyId(companyId)
            .and(feedback.id.eq(feedbackId))
            .fetchOne(feedbackDataMapper)
    }

    fun getFeedbackHistory(companyId: Long, employeeId: Long): List<FeedbackData> {
        return selectFeedbacksByCompanyId(companyId)
            .and(feedback.employeeId.eq(employeeId))
            .fetch(feedbackDataMapper)
    }

    fun getAllFeedbacks(companyId: Long): List<FeedbackData> {
        return selectFeedbacksByCompanyId(companyId)
            .fetch(feedbackDataMapper)
    }

    private fun selectFeedbacksByCompanyId(companyId: Long) = sql.select(
        feedback.id,
        feedback.companyId,
        feedback.employeeId,
        feedback.feedbackText,
        feedback.isAnonymous,
        feedback.department,
        feedback.createdAt,
        feedback.status
    )
        .from(feedback)
        .where(feedback.companyId.eq(companyId))
}