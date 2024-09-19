package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Book
import com.example.quo_digital_demo.services.BooksService
import com.example.quo_digital_demo.services.BooksService.Page
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Range
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 書籍情報を扱うコントローラー
 *
 * 方針については、AuthorsControllerのコメントに書きました。
 */
@Validated
@RestController
@RequestMapping("/books")
class BooksController(private val booksService: BooksService) {

    data class FindPageForm(
        @field: Min(1) val pageNum: Int = 1,
        @field: Range(min=1, max=20) val itemsPerPage: Int = 10
    )

    @GetMapping("/find_page")
    fun findPage(@Validated form: FindPageForm, result: BindingResult): Page {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.findPage(form.pageNum, form.itemsPerPage)
    }

    @GetMapping("/find")
    fun findById(@RequestParam id: UUID): Book? {
        return booksService.findById(id)
    }

    data class CreateForm(
        @field: NotBlank val title: String,
        @field: NotBlank val authorIds: List<UUID>
    )

    @PostMapping("/create")
    fun create(@Validated form: CreateForm, result: BindingResult): Book {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.create(form.title, form.authorIds)
    }

    data class UpdateForm(
        val id: UUID,
        @field: NotBlank val title: String,
        @field: NotBlank val authorIds: List<UUID>
    )

    @PostMapping("/update")
    fun update(@Validated form: UpdateForm, result: BindingResult): Int {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.update(Book(
            id = form.id,
            title = form.title,
            authorIds = form.authorIds))
    }

    @PostMapping("/delete")
    fun deleteById(@RequestParam id: UUID): Int {
        return booksService.deleteById(id)
    }

    private fun toException(result: BindingResult) =
        IllegalArgumentException(result.toString())
}
