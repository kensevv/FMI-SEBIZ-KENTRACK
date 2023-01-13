package com.fmi.kentrack.model

import java.time.LocalDateTime

data class Ticket(
    val id: Int,
    val title: String,
    val description: String,
    val owner: User,
    val assignee: User,
    val comments: List<Comment>,
    val section: Section,
    val board: Board,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
)