package com.fmi.kentrack.model

import java.time.LocalDateTime

data class Comment (
    val id: Int,
    val content: String,
    val author: User,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
)