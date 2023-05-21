package com.fmi.kentrack.users

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val roles: List<Roles> = emptyList()
)