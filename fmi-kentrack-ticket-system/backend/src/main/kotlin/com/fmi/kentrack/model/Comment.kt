package com.fmi.kentrack.model

import com.fmi.kentrack.users.User
import java.time.LocalDateTime

data class Comment (
    val id: Int,
    val content: String,
    val author: User? = null,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
)