package com.example.quo_digital_demo.repositories

import com.example.quo_digital_demo.generated.tables.Authors.Companion.AUTHORS
import com.example.quo_digital_demo.models.Author
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AuthorsRepositoryImpl(private val context: DSLContext) : AuthorsRepository {

    override fun countAll(): Int {
        return context
            .selectCount().from(AUTHORS)
            .fetchOne()?.into(Int::class.java)!!
    }

    override fun findWithOffsetLimit(offset: Int, limit: Int): List<Author> {
        return context
            .select().from(AUTHORS)
            .orderBy(AUTHORS.CREATED_AT)
            .limit(limit)
            .offset(offset)
            .fetch().map { toModel(it) }
    }

    override fun findById(id: UUID): Author? {
        return context
            .select().from(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .fetchOne()?.let { toModel(it) }
    }

    override fun create(author: Author): Int {
        return context
            .insertInto(AUTHORS)
            .columns(AUTHORS.FULL_NAME)
            .values(author.fullName)
            .execute()
    }

    override fun update(author: Author): Int {
        return context
            .update(AUTHORS)
            .set(AUTHORS.FULL_NAME, author.fullName)
            .where(AUTHORS.ID.eq(author.id))
            .execute()
    }

    override fun deleteById(id: UUID): Int {
        return context
            .delete(AUTHORS)
            .where(AUTHORS.ID.eq(id))
            .execute()
    }

    private fun toModel(record: Record) = Author(
        record.getValue(AUTHORS.ID),
        record.getValue(AUTHORS.FULL_NAME)!!,  // DBでNOT NULL制約
        record.getValue(AUTHORS.CREATED_AT),
        record.getValue(AUTHORS.UPDATED_AT))
}
