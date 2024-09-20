package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.generated.tables.Books.Companion.BOOKS
import com.example.quo_digital_demo.models.Book
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class BooksRepositoryImpl(private val context: DSLContext) : BooksRepository {

    /**
     * 著者に紐づく本の総数
     */
    override fun countAllByAuthorId(authorId: UUID): Int {
        return context
            .selectCount().from(BOOKS)
            .where(BOOKS.AUTHOR_IDS.contains(DSL.cast(DSL.array(authorId), BOOKS.AUTHOR_IDS.dataType)))
            .fetchOne()?.into(Int::class.java)!!
    }

    /**
     * 著者に紐づく本を取得
     */
    override fun findByAuthorIdWithOffsetLimit(authorId: UUID, offset: Int, limit: Int): List<Book> {
        // WHEREの部分をSQLで書くと以下 (他の条件式は省略)
        // SELECT * FROM books WHERE '{29a15ec5-6a0a-44c0-9a35-e85c7a472aea}' <@ author_ids;
        return context
            .select().from(BOOKS)
            .where(BOOKS.AUTHOR_IDS.contains(DSL.cast(DSL.array(authorId), BOOKS.AUTHOR_IDS.dataType)))
            .orderBy(BOOKS.CREATED_AT)
            .limit(limit)
            .offset(offset)
            .fetch().map { toModel(it) }
    }

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
