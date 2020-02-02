package com.soywiz.luak.compat.java.io

expect abstract class InputStream() : Closeable {
    abstract fun read(): Int

    open fun markSupported(): Boolean
    open fun mark(i: Int)
    open fun reset()
    open fun read(b: ByteArray): Int
    open fun read(b: ByteArray, off: Int, len: Int): Int
    open fun skip(n: Long): Long
    open fun available(): Int
    override fun close(): Unit
}

expect open class FilterInputStream : InputStream {
}

expect open class DataInputStream(iss: InputStream) : FilterInputStream {
    fun readByte(): Byte
    fun readUnsignedByte(): Int
    fun readFully(b: ByteArray, off: Int, len: Int)
}

expect open class ByteArrayInputStream(buf: ByteArray, start: Int, size: Int) : InputStream {
}
