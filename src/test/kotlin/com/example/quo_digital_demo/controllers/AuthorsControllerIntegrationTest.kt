package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Author
import com.example.quo_digital_demo.services.Page
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.util.LinkedMultiValueMap
import kotlin.test.Test

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:sql/AuthorsControllerIntegrationTest.sql")
@Sql(value = ["classpath:sql/clear_authors.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorsControllerIntegrationTest {

    @Autowired
    var template: TestRestTemplate? = null

    @Test
    fun test_findPage_success() {
        val response = template?.getForEntity("/authors/find_page?pageNum=1&itemsPerPage=10", Page::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)
        val page = response?.body
        assertThat(page?.items?.size == 1).isTrue()
        assertThat(page?.itemsTotalNum == 1).isTrue()
    }

    @Test
    fun test_findById_success() {

        val id = "29a15ec5-6a0a-44c0-9a35-e85c7a472aec"

        val response = template?.getForEntity("/authors/find?id=$id", Author::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val author = response?.body
        assertThat(author).isNotNull()
        assertThat(author?.id.toString() == id).isTrue()
        assertThat(author?.fullName == "Taro Yamada").isTrue()
    }

    @Test
    fun test_findById_null() {

        val id = "11111111-1111-1111-1111-111111111111"

        val response = template?.getForEntity("/authors/find?id=$id", Author::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val author = response?.body
        assertThat(author).isNull()
    }

    @Test
    fun test_create() {

        val fullName = "Ken Tanaka"

        val form = LinkedMultiValueMap<String, Any>()
        form.add("fullName", fullName)

        val response = template?.postForEntity("/authors/create", form, Author::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        val author = response?.body
        assertThat(author).isNotNull()
        assertThat(author?.fullName == fullName).isTrue()
    }

    @Test
    fun test_update() {

        val form = LinkedMultiValueMap<String, Any>()
        form.add("id", "29a15ec5-6a0a-44c0-9a35-e85c7a472aec")
        form.add("fullName", "Terry Ito")

        val response = template?.postForEntity("/authors/update", form, Integer::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(response?.body).isEqualTo(Integer.valueOf(1))
    }

    @Test
    fun test_deleteById() {

        val form = LinkedMultiValueMap<String, Any>()
        form.add("id", "29a15ec5-6a0a-44c0-9a35-e85c7a472aec")

        val response = template?.postForEntity("/authors/delete", form, Integer::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(response?.body).isEqualTo(Integer.valueOf(1))
    }
}
