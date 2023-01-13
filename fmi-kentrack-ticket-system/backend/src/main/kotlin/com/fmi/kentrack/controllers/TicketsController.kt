package com.fmi.kentrack.controllers

import com.fmi.kentrack.model.Board
import com.fmi.kentrack.model.Ticket
import com.fmi.kentrack.services.BoardsService
import com.fmi.kentrack.services.TicketsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api")
class TicketsController {
    @Autowired
    private lateinit var ticketsService: TicketsService

    @GetMapping("/get-all-tickets")
    fun getAllTickets() = ticketsService.getAllTickets()

    @PostMapping("/update-ticket")
    fun updateTicket(ticket: Ticket) = ticketsService.updateTicket(ticket)

    @PostMapping("/create-ticket")
    fun createTicket(newTicket: Ticket) = ticketsService.createTicket(newTicket)
}