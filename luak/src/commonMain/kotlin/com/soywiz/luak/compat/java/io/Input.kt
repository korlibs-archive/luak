package com.soywiz.luak.compat.java.io

expect abstract class InputStream() : Closeable {
    abstract fun read(): Int

    open fun markSupported(): Boolean
    open fun mark(i: Int)
    open fun reset()
    open fun read(b: ByteArray): Int
    open fun read(b: ByteArray, i0: Int, n: Int): Int
    open fun skip(n: Long): Long
    open fun available(): Int
    override fun close(): Unit
}

expect open class FilterInputStream : InputStream {
}

expect open class DataInputStream(iss: InputStream) : FilterInputStream {
    fun readByte(): Byte
    fun readUnsignedByte(): Int
    fun readFully(buf: ByteArray, i: Int, i1: Int)
}

expect open class ByteArrayInputStream(buf: ByteArray, offset: Int, size: Int) : InputStream {
}
