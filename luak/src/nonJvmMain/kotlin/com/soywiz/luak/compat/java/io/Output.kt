package com.soywiz.luak.compat.java.io

actual abstract class OutputStream() : Closeable {
    abstract fun write(value: Int)
    open fun write(b: ByteArray): Unit = TODO()
    open fun write(b: ByteArray, i: Int, size: Int): Unit = TODO()
}

actual open class FilterOutputStream(val out: OutputStream) : OutputStream() {
    override fun write(value: Int) {
        out.write(value)
    }

    override fun close() {
        out.close()
    }
}
