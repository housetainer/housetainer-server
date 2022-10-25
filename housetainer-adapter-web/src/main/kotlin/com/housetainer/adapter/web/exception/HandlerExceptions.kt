package com.housetainer.adapter.web.exception

import com.housetainer.domain.entity.exception.BaseException
import org.springframework.http.HttpStatus

object HandlerExceptions {

    private fun HttpStatus.exception(msg: String?): BaseException = BaseException(this.value(), msg)

    @JvmStatic
    fun emptyBodyException() = HttpStatus.BAD_REQUEST.exception("Body should not be empty")
}
