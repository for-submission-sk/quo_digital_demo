package com.example.quo_digital_demo.controllers

import com.example.quo_digital_demo.models.Author
import com.example.quo_digital_demo.services.AuthorsService.Page
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.net.URI
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
        assertThat(page?.authors?.size == 1).isTrue()
        assertThat(page?.numberOfAllAuthors == 1).isTrue()
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

    }

    @Test
    fun test_update() {

    }

    @Test
    fun test_deleteById() {
        val map: MultiValueMap<String, Any> = LinkedMultiValueMap()
        map.add("id", "29a15ec5-6a0a-44c0-9a35-e85c7a472aec")
        val uri = URI.create("/authors/delete")
        val response = template?.postForEntity(uri, map, String::class.java)
        assertThat(response?.statusCode).isEqualTo(HttpStatus.OK)
    }
}
