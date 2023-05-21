package com.fmi.kentrack.model

import com.fmi.kentrack.users.User
import java.time.LocalDateTime

data class Ticket(
    val id: Int? = null,
    val title: String,
    val description: String,
    val owner: User? = null,
    val assignee: User? = null,
    val comments: List<Comment>? = null,
    val section: Section? = null,
    val board: Board? = null,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
)