package com.housetainer.adapter.persistence.webclient.exception

import com.housetainer.domain.entity.exception.BaseException
import org.springframework.http.HttpStatus

class RestException(val httpStatus: HttpStatus) : BaseException(httpStatus.value(), httpStatus.reasonPhrase) {

    constructor(status: HttpStatus, message: String?) : this(status) {
        this.message = message
    }
}
