package com.soywiz.luak.compat.java.io

expect open class IOException : Exception {
    constructor()
    constructor(message: String)
}

expect open class EOFException : IOException {
    constructor()
    constructor(message: String)
}

expect open class UnsupportedEncodingException : IOException {
    constructor()
    constructor(message: String)
}
