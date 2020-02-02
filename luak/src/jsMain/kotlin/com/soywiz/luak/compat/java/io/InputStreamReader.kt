package com.soywiz.luak.compat.java.io

actual abstract class Reader() : Closeable {
    actual open fun read(): Int = TODO()
    actual abstract fun read(cbuf: CharArray, off: Int, len: Int): Int
    actual open fun read(cbuf: CharArray): Int = TODO()
}

actual open class BufferedReader actual constructor(val r: Reader) : Reader() {
    actual open fun readLine(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        r.close()
    }
}

actual open class InputStreamReader : Reader {
    actual constructor(iss: InputStream) {
        TODO()
    }
    actual constructor(iss: InputStream, encoding: String?) {
        TODO()
    }
    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
