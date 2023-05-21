package com.fmi.kentrack.services

import com.fmi.kentrack.jooq.tables.records.CommentRecord
import com.fmi.kentrack.jooq.tables.records.UserRecord
import com.fmi.kentrack.jooq.tables.references.COMMENT
import com.fmi.kentrack.jooq.tables.references.USER
import com.fmi.kentrack.model.Comment
import com.fmi.kentrack.users.mapToInternalModel
import org.jooq.impl.DSL
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class CommentsService : BaseService() {
    fun getCommentsForByTicketId(id: BigDecimal): List<Comment> =
        db.select(COMMENT.asterisk(), USER.asterisk())
            .from(COMMENT)
            .leftJoin(USER).on(COMMENT.AUTHOR_USERNAME.eq(USER.USERNAME))
            .where(COMMENT.TICKET_ID.eq(id)).fetch().map {
                it.into(CommentRecord::class.java).into(Comment::class.java).copy(
                    author = it.into(UserRecord::class.java).mapToInternalModel()
                )
            }

    fun getCommentById(id: BigDecimal): Comment =
        db.select(COMMENT.asterisk(), USER.asterisk())
            .from(COMMENT)
            .leftJoin(USER).on(COMMENT.AUTHOR_USERNAME.eq(USER.USERNAME))
            .where(COMMENT.ID.eq(id)).fetchSingle().map {
                it.into(CommentRecord::class.java).into(Comment::class.java).copy(
                    author = it.into(UserRecord::class.java).mapToInternalModel()
                )
            }

    fun addNewCommentToTicket(content: String, ticketId: BigDecimal, currentUserUsername: String): Comment {
        val newCommentId = getCommentsSeqNextVal()
        db.newRecord(COMMENT).apply {
            id = newCommentId
            authorUsername = currentUserUsername
            this.content = content
            this.ticketId = ticketId
            createdDate = LocalDateTime.now()
            updatedDate = LocalDateTime.now()
        }.run { insert() }
        return getCommentById(newCommentId)
    }

    fun deleteAllCommentsByTicketId(ticketId: BigDecimal) {
        db.deleteFrom(COMMENT).where(COMMENT.TICKET_ID.eq(ticketId)).execute()
    }

    private fun getCommentsSeqNextVal(): BigDecimal =
        db.select(DSL.field("COMMENTS_SEQUENCE.nextval")).from("DUAL")
            .fetchOne()!!.map { it.into(BigDecimal::class.java) }

}