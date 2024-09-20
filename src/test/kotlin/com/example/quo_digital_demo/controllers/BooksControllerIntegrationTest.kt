package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Book
import com.example.quo_digital_demo.services.Page
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.util.LinkedMultiValueMap
import java.util.UUID
import kotlin.test.Test

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:sql/BooksControllerIntegrationTest.sql")
@Sql(value = ["classpath:sql/clear_books.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BooksControllerIntegrationTest {

    @Autowired
    var template: TestRestTemplate? = null

    @Test
    fun test_findPageByAuthorId_success() {
        val response = template?.getForEntity(
            "/books/find_page_by_author_id?authorId=1a870220-9ffa-425b-8269-6e959afe623f&pageNum=1&itemsPerPage=10",
            Page::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)
        val page = response?.body
        assertThat(page?.items?.size == 1).isTrue()
        assertThat(page?.itemsTotalNum == 1).isTrue()
    }

    @Test
    fun test_findPage_success() {
        val response = template?.getForEntity("/books/find_page?pageNum=1&itemsPerPage=10", Page::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)
        val page = response?.body
        assertThat(page?.items?.size == 2).isTrue()
        assertThat(page?.itemsTotalNum == 2).isTrue()
    }

    @Test
    fun test_findById_success() {

        val id = "c82baf6a-c870-421c-babd-b68f414d4374"

        val response = template?.getForEntity("/books/find?id=$id", Book::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val book = response?.body
        assertThat(book).isNotNull()
        assertThat(book?.id.toString() == id).isTrue()
        assertThat(book?.title == "Very good book").isTrue()
    }

    @Test
    fun test_findById_null() {

        val id = "11111111-1111-1111-1111-111111111111"

        val response = template?.getForEntity("/books/find?id=$id", Book::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val book = response?.body
        assertThat(book).isNull()
    }

    @Test
    fun test_create() {

        val title = "Bad book"
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()

        val form = LinkedMultiValueMap<String, Any>()
        form.add("title", title)
        form.add("authorIds", "$id1,$id2")

        val response = template?.postForEntity("/books/create", form, Book::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val book = response?.body
        assertThat(book).isNotNull()
        assertThat(book?.title == title).isTrue()
        assertThat(book?.authorIds?.size == 2).isTrue()
        assertThat(book?.authorIds!![0] == id1).isTrue()
        assertThat(book.authorIds[1] == id2).isTrue()
    }

    @Test
    fun test_update() {

        val title = "Best book"
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()

        val form = LinkedMultiValueMap<String, Any>()
        form.add("id", "c82baf6a-c870-421c-babd-b68f414d4374")
        form.add("title", title)
        form.add("authorIds", "$id1,$id2")

        val response = template?.postForEntity("/books/update", form, Integer::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(response?.body).isEqualTo(Integer.valueOf(1))
    }

    @Test
    fun test_deleteById() {

        val form = LinkedMultiValueMap<String, Any>()
        form.add("id", "c82baf6a-c870-421c-babd-b68f414d4374")

        val response = template?.postForEntity("/books/delete", form, Integer::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(response?.body).isEqualTo(Integer.valueOf(1))
    }
}
