package com.example.quo_digital_demo.services

import com.example.quo_digital_demo.models.Book
import com.example.quo_digital_demo.repositories.BooksRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BooksServiceImpl(private val booksRepository: BooksRepository) : BooksService {

    override fun findPageByAuthorId(authorId: UUID, pageNum: Int, itemsPerPage: Int): Page<Book> {
        val offset = (pageNum - 1) * itemsPerPage
        val books = booksRepository.findByAuthorIdWithOffsetLimit(authorId, offset, itemsPerPage)
        val allNum = booksRepository.countAllByAuthorId(authorId)
        return Page(books, allNum)
    }

    override fun findPage(pageNum: Int, itemsPerPage: Int): Page<Book> {
        val offset = (pageNum - 1) * itemsPerPage
        val books = booksRepository.findWithOffsetLimit(offset, itemsPerPage)
        val allNum = booksRepository.countAll()
        return Page(books, allNum)
    }

    override fun findById(id: UUID): Book? {
        return booksRepository.findById(id)
    }

    override fun create(book: Book): Int {
        return booksRepository.create(book)
    }

    override fun update(book: Book): Int {
        return booksRepository.update(book)
    }

    override fun deleteById(id: UUID): Int {
        return booksRepository.deleteById(id)
    }
}
