package com.example.quo_digital_demo.models

import java.time.OffsetDateTime
import java.util.UUID

data class Book(
    val id: UUID? = null,
    val title: String,
    val authorIds: List<UUID>,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null)
