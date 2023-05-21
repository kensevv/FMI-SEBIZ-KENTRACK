package com.fmi.kentrack.model

import com.fmi.kentrack.users.User
import java.time.LocalDateTime

data class Board(
    val id: Int? = null,
    val title: String,
    val owner: User? = null,
    val participants: List<User>? = null,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime
)