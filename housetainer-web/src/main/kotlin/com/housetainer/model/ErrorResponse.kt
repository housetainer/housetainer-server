package com.housetainer.model

data class ErrorResponse(
    val requestId: String,
    val code: Int,
    val message: String?
)
