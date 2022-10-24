package com.housetainer.adapter.persistence.exception

import com.housetainer.domain.entity.exception.BaseException

object PersistenceExceptions {

    @JvmStatic
    fun unexpectedException() = BaseException(500, "unexpected error occurs")

    @JvmStatic
    fun forbiddenException() = BaseException(403)

    fun userNotFound() = BaseException(404, "user not found")
}
