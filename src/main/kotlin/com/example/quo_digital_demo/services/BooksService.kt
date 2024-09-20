package com.example.quo_digital_demo.services

import com.example.quo_digital_demo.models.Book
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface BooksService {
    fun findPageByAuthorId(authorId: UUID, pageNum: Int, itemsPerPage: Int): Page<Book>
    fun findPage(pageNum: Int, itemsPerPage: Int): Page<Book>
    fun findById(id: UUID): Book?
    fun create(title: String, authorIds: List<UUID>): Book
    fun update(book: Book): Int
    fun deleteById(id: UUID): Int
}
