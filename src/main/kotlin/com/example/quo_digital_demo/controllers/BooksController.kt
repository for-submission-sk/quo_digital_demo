package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Book
import com.example.quo_digital_demo.services.BooksService
import com.example.quo_digital_demo.services.Page
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

    data class FindPageByAuthorIdForm(
        val authorId: UUID,
        @field: Min(1) val pageNum: Int = 1,
        @field: Range(min=1, max=20) val itemsPerPage: Int = 10
    )

    /**
     * 著者に紐づく書籍情報の一覧を取得
     */
    @GetMapping("/find_page_by_author_id")
    fun findPageByAuthorId(@Validated form: FindPageByAuthorIdForm, result: BindingResult): Page<Book> {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.findPageByAuthorId(
            form.authorId,
            form.pageNum,
            form.itemsPerPage)
    }

    data class FindPageForm(
        @field: Min(1) val pageNum: Int = 1,
        @field: Range(min=1, max=20) val itemsPerPage: Int = 10
    )

    /**
     * 書籍情報の一覧を取得
     */
    @GetMapping("/find_page")
    fun findPage(@Validated form: FindPageForm, result: BindingResult): Page<Book> {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.findPage(form.pageNum, form.itemsPerPage)
    }

    /**
     * 特定の書籍情報を取得
     */
    @GetMapping("/find")
    fun findById(@RequestParam id: UUID): Book? {
        return booksService.findById(id)
    }

    data class CreateForm(
        @field: NotBlank val title: String,
        val authorIds: List<UUID>
    )

    /**
     * 書籍情報の登録
     */
    @PostMapping("/create")
    fun create(@Validated form: CreateForm, result: BindingResult): Int {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return booksService.create(
            form.title,
            form.authorIds)
    }

    data class UpdateForm(
        val id: UUID,
        @field: NotBlank val title: String,
        val authorIds: List<UUID>
    )

    /**
     * 書籍情報の更新
     */
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

    /**
     * 書籍情報の削除
     */
    @PostMapping("/delete")
    fun deleteById(@RequestParam id: UUID): Int {
        return booksService.deleteById(id)
    }

    private fun toException(result: BindingResult) =
        IllegalArgumentException(result.toString())
}
