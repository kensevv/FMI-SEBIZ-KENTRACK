package com.fmi.kentrack.services

import com.fmi.kentrack.jooq.tables.references.TICKET
import com.fmi.kentrack.model.Ticket
import org.springframework.stereotype.Service

@Service
class TicketsService : Base() {
    fun getAllTickets(): List<Ticket> = db.selectFrom(TICKET).fetchInto(Ticket::class.java)
    fun updateTicket(ticket: Ticket) {
        db.fetchOne(TICKET, TICKET.ID.eq(ticket.id.toBigDecimal()))?.apply {
            from(ticket)
        }?.update()

    }

    fun createTicket(newTicket: Ticket) {
        db.newRecord(TICKET).apply {
            from(newTicket)
        }.insert()
    }
}