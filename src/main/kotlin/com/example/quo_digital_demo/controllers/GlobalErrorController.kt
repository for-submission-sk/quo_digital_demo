package com.example.quo_digital_demo.controllers

import jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI
import jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.BINDING_ERRORS
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.EXCEPTION
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.STACK_TRACE
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import java.util.Date

/**
 * stackTraceのような必要以上の情報を外部に公開しないため定義
 * ステータスコードも外部向けは404を除き500に変更
 * 内部向けには詳細をログファイルに出力 (内容の整形は時間の都合により省略)
 */
@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class GlobalErrorController : ErrorController {

    private val logger = LoggerFactory.getLogger(GlobalErrorController::class.java) as Logger

    /**
     * エラーページのパス
     */
    @Value("\${server.error.path:\${error.path:/error}}")
    val errorPath: String? = null

    /**
     * バックエンド全体の例外を処理するハンドラ
     *
     * @param request HTTPリクエスト
     * @return HTTPレスポンス用のJSON
     */
    @RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun respondByErrorJson(request: HttpServletRequest): ResponseEntity<Map<String, Any>> {

        logErrorAttributes(request)

        val statusCode = request.getAttribute(ERROR_STATUS_CODE)  // java.lang.Integer
        val status =
            if (statusCode == null || statusCode.toString() != "404") {
                INTERNAL_SERVER_ERROR
            } else {
                NOT_FOUND
            }

        val body = HashMap<String, Any>()
        body["timestamp"] = Date()
        body["status"] = status.value()
        body["path"] = request.getAttribute(ERROR_REQUEST_URI)

        return ResponseEntity(body, status)
    }

    /**
     * エラーの内容をログに出力
     * @param request HTTPリクエスト
     */
    private fun logErrorAttributes(request: HttpServletRequest) {

        val attr = DefaultErrorAttributes().getErrorAttributes(
            ServletWebRequest(request),
            ErrorAttributeOptions.of(BINDING_ERRORS, EXCEPTION, MESSAGE, STACK_TRACE))

        // 本来はここでログの内容を整えますが、時間の都合により省略 ("logback.xml"も最低限です)

        logger.error(attr.toString())
    }
}
