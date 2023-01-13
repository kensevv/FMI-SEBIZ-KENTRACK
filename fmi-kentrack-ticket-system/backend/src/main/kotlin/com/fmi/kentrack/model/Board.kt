package com.fmi.kentrack.model

import java.time.LocalDateTime

data class Board(
    val id: Int,
    val title: String,
    val owner: User,
    val participants: List<User>,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime
)