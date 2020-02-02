package com.soywiz.luak.compat.java.io

actual abstract class InputStream actual constructor() : Closeable {
    actual abstract fun read(): Int

    actual open fun markSupported(): Boolean = false
    actual open fun mark(i: Int) = Unit
    actual open fun reset() = Unit
    actual open fun read(b: ByteArray, off: Int, len: Int): Int {
        for (n in 0 until len) {
            val c = read()
            if (c < 0) return n
            b[off + n] = c.toByte()
        }
        return len
    }
    actual open fun read(b: ByteArray): Int = read(b, 0, b.size)
    actual open fun skip(n: Long): Long {
        for (cur in 0L until n) if (read() < 0) return cur
        return n
    }
    actual open fun available(): Int = 0
    actual override fun close() = Unit
}

actual open class FilterInputStream(val iss: InputStream) : InputStream() {
    override fun read(): Int = iss.read()
    override fun close() = iss.close()
}

actual open class DataInputStream actual constructor(iss: InputStream) : FilterInputStream(iss) {
    actual fun readFully(b: ByteArray, off: Int, len: Int) {
        var count: Int
        var n = 0
        while (n < len) {
            count = iss.read(b, off + n, len - n)
            if (count < 0) throw EOFException()
            n += count
        }
    }

    actual fun readByte(): Byte = iss.read().let { c -> if (c < 0) throw EOFException() else c.toByte() }
    actual fun readUnsignedByte(): Int = readByte().toInt() and 0xFF
}
