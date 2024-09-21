package com.example.quo_digital_demo.services

import com.example.quo_digital_demo.models.Author
import com.example.quo_digital_demo.repositories.AuthorsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthorsServiceImpl(private val authorsRepository: AuthorsRepository) : AuthorsService {

    override fun findPage(pageNum: Int, itemsPerPage: Int): Page<Author> {
        val offset = (pageNum - 1) * itemsPerPage
        val authors = authorsRepository.findWithOffsetLimit(offset, itemsPerPage)
        val allNum = authorsRepository.countAll()
        return Page(authors, allNum)
    }

    override fun findById(id: UUID): Author? {
        return authorsRepository.findById(id)
    }

    override fun create(author: Author): Int {
        return authorsRepository.create(author)
    }

    override fun update(author: Author): Int {
        return authorsRepository.update(author)
    }

    override fun deleteById(id: UUID): Int {
        return authorsRepository.deleteById(id)
    }
}
