package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Author
import com.example.quo_digital_demo.services.AuthorsService
import com.example.quo_digital_demo.services.AuthorsService.Page
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/authors")
class AuthorsController(private val authorsService: AuthorsService) {

    // TODO: 引数のバリデーション
    @GetMapping("/find_page")
    fun findPage(@RequestParam pageNum: Int = 1, @RequestParam itemsPerPage: Int = 10): Page {
        return authorsService.findPage(pageNum, itemsPerPage)
    }

    @GetMapping("/find")
    fun findById(@RequestParam id: UUID): Author? {
        return authorsService.findById(id)
    }

    // TODO: fullNameのバリデーション
    @PostMapping("/create")
    fun create(@RequestParam fullName: String): Author {
        return authorsService.create(fullName)
    }

    // TODO: fullNameのバリデーション
    @PostMapping("/update")
    fun update(@RequestParam id: UUID, @RequestParam fullName: String): Int {
        return authorsService.update(Author(id = id, fullName = fullName))
    }

    @PostMapping("/delete")
    fun deleteById(id: UUID): Int {
        return authorsService.deleteById(id)
    }
}
