package com.soywiz.luak.compat.java.io

actual abstract class Reader() : Closeable {
    actual abstract fun read(cbuf: CharArray, off: Int, len: Int): Int
    actual open fun read(cbuf: CharArray): Int = read(cbuf, 0, cbuf.size)
    actual open fun read(): Int = CharArray(1).let { c -> read(c); c[0].toInt() }
}

actual open class InputStreamReader actual constructor(val iss: InputStream, val encoding: String?) : Reader() {
    actual constructor(iss: InputStream) : this(iss, null)

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        // @TODO: This reads ASCII
        for (n in 0 until len) {
            val c = read()
            if (c >= 0) {
                cbuf[off + n] = c.toChar()
            }
            return n
        }
        return len
    }

    override fun close() = iss.close()
}
