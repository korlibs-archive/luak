package com.soywiz.luak.compat.java.io

actual abstract class InputStream() : Closeable {
    abstract fun read(): Int

    open fun markSupported(): Boolean = false
    open fun mark(i: Int) = Unit
    open fun reset() = Unit
    open fun read(b: ByteArray): Int = TODO()
    open fun read(b: ByteArray, i0: Int, n: Int): Int = TODO()
    open fun skip(n: Long): Long = TODO()
    open fun available(): Int = TODO()
}
