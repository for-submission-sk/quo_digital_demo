package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.models.Book
import java.util.UUID

interface BooksRepository {
    fun countAllByAuthorId(authorId: UUID): Int
    fun findByAuthorIdWithOffsetLimit(authorId: UUID, offset: Int, limit: Int): List<Book>
    fun countAll(): Int
    fun findWithOffsetLimit(offset: Int, limit: Int): List<Book>
    fun findById(id: UUID): Book?
    fun create(title: String, authorIds: List<UUID>): Book
    fun update(book: Book): Int
    fun deleteById(id: UUID): Int
}
