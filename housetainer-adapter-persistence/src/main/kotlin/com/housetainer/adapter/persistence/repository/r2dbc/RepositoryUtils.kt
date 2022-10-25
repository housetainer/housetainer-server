package com.housetainer.adapter.persistence.repository.r2dbc

import com.housetainer.adapter.persistence.exception.PersistenceExceptions
import com.housetainer.common.log.logger
import io.r2dbc.spi.R2dbcException
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

object RepositoryUtils {

    val log = logger()

    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-error-sqlstates.html
    inline fun <reified T, reified U : CoroutineCrudRepository<T, Long>> U.execute(block: (U) -> T): T {
        return runCatching {
            block(this)
        }.onFailure {
            val throwable = it.cause ?: it
            if (throwable is R2dbcException) {
                log.error(
                    "db-exception, code={}, sqlState={}, cause={}",
                    throwable.errorCode,
                    throwable.sqlState,
                    throwable.message
                )
                throw PersistenceExceptions.forbiddenException()
            } else {
                log.error("unexpected-db-exception", throwable)
                throw PersistenceExceptions.unexpectedException()
            }
        }.getOrThrow()
    }
}
