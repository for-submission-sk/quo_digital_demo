package com.example.quo_digital_demo.services

import com.example.quo_digital_demo.models.Book
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface BooksService {

    data class Page(
        val books: List<Book>,
        val numberOfAllBooks: Int)

    fun findPage(pageNum: Int, itemsPerPage: Int): Page
    fun findById(id: UUID): Book?
    fun create(title: String, authorIds: List<UUID>): Book
    fun update(book: Book): Int
    fun deleteById(id: UUID): Int
}
