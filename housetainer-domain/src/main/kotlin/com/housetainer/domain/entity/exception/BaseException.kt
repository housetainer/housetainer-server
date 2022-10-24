package com.housetainer.domain.entity.exception

open class BaseException(
    val code: Int,
    override var message: String? = null
) : RuntimeException(message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseException

        if (code != other.code) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}
