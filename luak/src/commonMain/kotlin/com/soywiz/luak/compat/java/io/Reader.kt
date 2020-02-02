package com.soywiz.luak.compat.java.io

expect abstract class Reader {
    open fun read(): Int
    abstract fun read(cbuf: CharArray, off: Int, len: Int): Int
    open fun read(cbuf: CharArray): Int
    abstract fun close()
}

expect open class BufferedReader(r: Reader) : Reader {
    open fun readLine(): String?
}

expect open class InputStreamReader(iss: InputStream, encoding: String?) : Reader {
}
