package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Author
import com.example.quo_digital_demo.services.AuthorsService
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
 * 著者情報を扱うコントローラー
 *
 * HTTPのパラメーター (このクラスのpublicメソッドの引数) の不正については、
 * 実装にかかる時間も考慮し、今回は以下の方針で書きます。
 * ・アプリ側にはエラーが起きた事しか伝えない (404と500の区別のみ)
 * ・サーバ側のログファイルに詳細を残す
 * ・個々のコントローラーでは例外を投げるのみとし、GlobalErrorControllerで処理する
 *
 * 業務では、アプリ側に返す情報は、デバッグし易さとセキュリティのバランスを考え、プロジェクトの方針に合わせます。
 */
@Validated
@RestController
@RequestMapping("/authors")
class AuthorsController(private val authorsService: AuthorsService) {

    data class FindPageForm(
        @field: Min(1) val pageNum: Int = 1,
        @field: Range(min=1, max=20) val itemsPerPage: Int = 10
    )

    /**
     * 著者情報の一覧を取得
     */
    @GetMapping("/find_page")
    fun findPage(@Validated form: FindPageForm, result: BindingResult): Page<Author> {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return authorsService.findPage(form.pageNum, form.itemsPerPage)
    }

    /**
     * 特定の著者情報を取得
     */
    @GetMapping("/find")
    fun findById(@RequestParam id: UUID): Author? {
        return authorsService.findById(id)
    }

    data class CreateForm(
        @field: NotBlank val fullName: String
    )

    /**
     * 著者情報の登録
     */
    @PostMapping("/create")
    fun create(@Validated form: CreateForm, result: BindingResult): Int {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return authorsService.create(Author(
            fullName = form.fullName))
    }

    data class UpdateForm(
        val id: UUID,
        @field: NotBlank val fullName: String
    )

    /**
     * 著者情報の更新
     */
    @PostMapping("/update")
    fun update(@Validated form: UpdateForm, result: BindingResult): Int {
        if (result.hasErrors()) {
            throw toException(result)
        }
        return authorsService.update(Author(
            id = form.id,
            fullName = form.fullName))
    }

    /**
     * 著者情報の削除
     */
    @PostMapping("/delete")
    fun deleteById(@RequestParam id: UUID): Int {
        return authorsService.deleteById(id)
    }

    private fun toException(result: BindingResult) =
        IllegalArgumentException(result.toString())
}
