package com.fmi.kentrack.controllers

import com.fmi.kentrack.model.Ticket
import com.fmi.kentrack.services.CommentsService
import com.fmi.kentrack.services.TicketsService
import kotlinx.serialization.json.JsonNull.content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.security.Principal

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api")
class TicketsController {
    @Autowired
    private lateinit var ticketsService: TicketsService

    @Autowired
    private lateinit var commentsService: CommentsService

    @GetMapping("/get-all-tickets")
    fun getAllTickets() = ticketsService.getAllTickets()

    @PostMapping("/update-ticket")
    fun updateTicket(@RequestBody ticket: Ticket) = ticketsService.updateTicket(ticket)

    @PostMapping("/create-new-ticket")
    fun createTicket(@RequestBody newTicket: Ticket) = ticketsService.createTicket(newTicket)

    @PostMapping("/create-new-comment-for-ticket")
    fun addNewCommentToTicket(
        @RequestParam commentContent: String,
        @RequestParam ticketId: BigDecimal,
        principal: Principal
    ) = commentsService.addNewCommentToTicket(commentContent, ticketId, principal.name)
    @DeleteMapping("/delete-ticket-by-id")
    fun deleteTicketById(
        @RequestParam ticketId: BigDecimal,
    ) = ticketsService.deleteTicketById(ticketId)

}