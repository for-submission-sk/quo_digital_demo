package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.models.Author
import java.util.UUID

interface AuthorsRepository {
    fun countAll(): Int
    fun findWithOffsetLimit(offset: Int, limit: Int): List<Author>
    fun findById(id: UUID): Author?
    fun create(author: Author): Int
    fun update(author: Author): Int
    fun deleteById(id: UUID): Int
}
