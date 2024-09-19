package com.example.quo_digital_demo.services

import com.example.quo_digital_demo.models.Author
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface AuthorsService {

    data class Page(
        val authors: List<Author>,
        val numberOfAllAuthors: Int)

    fun findPage(pageNum: Int, itemsPerPage: Int): Page
    fun findById(id: UUID): Author?
    fun create(fullName: String): Author
    fun update(author: Author): Int
    fun deleteById(id: UUID): Int
}
