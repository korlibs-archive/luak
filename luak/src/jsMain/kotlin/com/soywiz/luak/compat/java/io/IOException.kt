package com.soywiz.luak.compat.java.io

actual open class IOException : Exception {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

actual open class EOFException : IOException {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

actual open class UnsupportedEncodingException : IOException {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}
