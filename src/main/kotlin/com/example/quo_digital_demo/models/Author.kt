package com.example.quo_digital_demo.models

import java.time.OffsetDateTime
import java.util.UUID

data class Author(
    val id: UUID? = null,
    val fullName: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null)
