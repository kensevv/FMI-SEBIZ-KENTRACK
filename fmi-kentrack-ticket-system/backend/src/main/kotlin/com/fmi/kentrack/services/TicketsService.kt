package com.fmi.kentrack.services

import com.fmi.kentrack.jooq.tables.records.SectionRecord
import com.fmi.kentrack.jooq.tables.records.TicketRecord
import com.fmi.kentrack.jooq.tables.records.UserRecord
import com.fmi.kentrack.jooq.tables.references.SECTION
import com.fmi.kentrack.jooq.tables.references.TICKET
import com.fmi.kentrack.jooq.tables.references.USER
import com.fmi.kentrack.model.Section
import com.fmi.kentrack.model.Ticket
import com.fmi.kentrack.users.mapToInternalModel
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TicketsService : BaseService() {
    @Autowired
    private lateinit var boardService: BoardsService

    @Autowired
    private lateinit var commentsService: CommentsService

    companion object {
        private val TICKET_OWNER_ALIAS = USER.`as`("TicketOwner")
        private val TICKET_ASSIGNEE_ALIAS = USER.`as`("TicketAssignee")
    }

    fun getAllTickets(): List<Ticket> = with(TICKET) {
        getTicketsSelectConditionStep().fetch().map {
            mapRecordToInternalModel(it)
        }
    }

    fun getTicketById(id: BigDecimal): Ticket = with(TICKET) {
        getTicketsSelectConditionStep().where(this.ID.eq(id)).fetchSingle().map {
            mapRecordToInternalModel(it)
        }
    }

    fun updateTicket(ticket: Ticket) {
        db.fetchOne(TICKET, TICKET.ID.eq(ticket.id?.toBigDecimal()))?.apply {
            from(ticket)
            ownerUsername = ticket.owner?.username
            assigneeUsername = ticket.assignee?.username
            sectionTitle = ticket.section?.sectionName
            boardId = ticket.board?.id?.toBigDecimal()
            updatedDate = LocalDateTime.now()
        }?.update()
    }

    fun createTicket(newTicket: Ticket): Ticket {
        val newId = getTicketsSeqNextVal()
        db.newRecord(TICKET, newTicket).apply {
            from(newTicket)
            id = newId
            ownerUsername = newTicket.owner?.username
            assigneeUsername = newTicket.assignee?.username
            sectionTitle = newTicket.section?.sectionName
            boardId = newTicket.board?.id?.toBigDecimal()
            createdDate = LocalDateTime.now()
            updatedDate = LocalDateTime.now()
        }.insert()
        return getTicketById(newId)
    }


    private fun mapRecordToInternalModel(
        it: Record,
    ): Ticket {
        val ticketRecord = it.into(TicketRecord::class.java)
        return ticketRecord.into(Ticket::class.java).copy(
            owner = it.into(TICKET_OWNER_ALIAS).into(UserRecord::class.java).mapToInternalModel(),
            assignee = it.into(TICKET_ASSIGNEE_ALIAS).into(UserRecord::class.java).mapToInternalModel(),
            section = it.into(SECTION).into(SectionRecord::class.java).into(Section::class.java),
            comments = commentsService.getCommentsForByTicketId(ticketRecord.id!!),
            board = boardService.getBoardById(ticketRecord.boardId!!)
        )
    }

    private fun com.fmi.kentrack.jooq.tables.Ticket.getTicketsSelectConditionStep() = db.select(
        this.asterisk(),
        TICKET_OWNER_ALIAS.asterisk(),
        TICKET_ASSIGNEE_ALIAS.asterisk(),
        SECTION.asterisk(),
    )
        .from(this)
        .leftJoin(TICKET_OWNER_ALIAS).on(this.OWNER_USERNAME.eq(TICKET_OWNER_ALIAS.USERNAME))
        .leftJoin(TICKET_ASSIGNEE_ALIAS).on(this.ASSIGNEE_USERNAME.eq(TICKET_ASSIGNEE_ALIAS.USERNAME))
        .leftJoin(SECTION).on(this.SECTION_TITLE.eq(SECTION.SECTION_NAME))

    private fun getTicketsSeqNextVal(): BigDecimal =
        db.select(DSL.field("TICKETS_SEQUENCE.nextval")).from("DUAL")
            .fetchOne()!!.map { it.into(BigDecimal::class.java) }

    fun deleteTicketById(ticketId: BigDecimal) {
        commentsService.deleteAllCommentsByTicketId(ticketId)
        db.deleteFrom(TICKET).where(TICKET.ID.eq(ticketId)).execute()
    }

}