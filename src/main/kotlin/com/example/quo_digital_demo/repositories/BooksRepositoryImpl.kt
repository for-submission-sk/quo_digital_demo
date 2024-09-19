package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.generated.tables.Books.Companion.BOOKS
import com.example.quo_digital_demo.models.Book
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class BooksRepositoryImpl(private val context: DSLContext) : BooksRepository {

    override fun countAll(): Int {
        return context
            .selectCount().from(BOOKS)
            .fetchOne()?.into(Int::class.java)!!
    }

    override fun findByOffsetLimit(offset: Int, limit: Int): List<Book> {
        return context
            .select().from(BOOKS)
            .orderBy(BOOKS.CREATED_AT)
            .limit(limit)
            .offset(offset)
            .fetch().map { toModel(it) }
    }

    override fun findById(id: UUID): Book? {
        return context
            .select().from(BOOKS)
            .where(BOOKS.ID.eq(id))
            .fetchOne()?.let { toModel(it) }
    }

    override fun create(title: String, authorIds: List<UUID>): Book {
        val record = context.newRecord(BOOKS).also {
            it.title = title
            it.authorIds = authorIds.toTypedArray()
            it.store()
        }
        return toModel(record)
    }

    override fun update(book: Book): Int {
        return context
            .update(BOOKS)
            .set(BOOKS.TITLE, book.title)
            .set(BOOKS.AUTHOR_IDS, book.authorIds.toTypedArray())
            .where(BOOKS.ID.eq(book.id))
            .execute()
    }

    override fun deleteById(id: UUID): Int {
        return context
            .delete(BOOKS)
            .where(BOOKS.ID.eq(id))
            .execute()
    }

    private fun toModel(record: Record) = Book(
        record.getValue(BOOKS.ID)!!,  // DBでNOT NULL制約
        record.getValue(BOOKS.TITLE)!!,  // 同上
        record.getValue(BOOKS.AUTHOR_IDS)!!.filterNotNull().toList(),  // 同上
        record.getValue(BOOKS.CREATED_AT),
        record.getValue(BOOKS.UPDATED_AT)
    )
}
