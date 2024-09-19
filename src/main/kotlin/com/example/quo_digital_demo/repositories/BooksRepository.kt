package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.models.Book
import java.util.UUID

interface BooksRepository {
    fun countAll(): Int
    fun findByOffsetLimit(offset: Int, limit: Int): List<Book>
    fun findById(id: UUID): Book?
    fun create(title: String, authorIds: List<UUID>): Book
    fun update(book: Book): Int
    fun deleteById(id: UUID): Int
}
