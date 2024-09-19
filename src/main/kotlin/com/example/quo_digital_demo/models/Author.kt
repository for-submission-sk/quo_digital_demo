package com.example.quo_digital_demo.models

import java.time.OffsetDateTime
import java.util.UUID

data class Author(
    val id: UUID,
    val fullName: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null)
