package com.github.radkoff26.nechto.exceptions

class LoadingException @JvmOverloads constructor(
    val errorCode: Int,
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)