package com.soywiz.luak.compat.java.io

expect abstract class Reader : Closeable {
    open fun read(): Int
    abstract fun read(cbuf: CharArray, off: Int, len: Int): Int
    open fun read(cbuf: CharArray): Int
}

expect open class BufferedReader(r: Reader) : Reader {
    open fun readLine(): String?
}

expect open class InputStreamReader : Reader {
    constructor(iss: InputStream, encoding: String?)
    constructor(iss: InputStream)
}
