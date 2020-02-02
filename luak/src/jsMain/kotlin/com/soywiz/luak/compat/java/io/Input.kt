package com.soywiz.luak.compat.java.io

actual abstract class InputStream actual constructor() : Closeable {
    actual abstract fun read(): Int

    actual open fun markSupported(): Boolean = false
    actual open fun mark(i: Int) = Unit
    actual open fun reset() = Unit
    actual open fun read(b: ByteArray): Int = TODO()
    actual open fun read(b: ByteArray, i0: Int, n: Int): Int = TODO()
    actual open fun skip(n: Long): Long = TODO()
    actual open fun available(): Int = TODO()
    actual override fun close() {
    }
}

actual open class FilterInputStream(val iss: InputStream) : InputStream() {
    override fun read(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual open class BufferedInputStream actual constructor(iss: InputStream) : FilterInputStream(iss) {
}

actual open class FileInputStream actual constructor(file: String) : InputStream() {
    override fun read(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual open class DataInputStream actual constructor(iss: InputStream) : FilterInputStream(iss) {
    actual fun readFully(buf: ByteArray, i: Int, i1: Int) {
    }

    actual fun readByte(): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun readUnsignedByte(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual open class ByteArrayInputStream actual constructor(buf: ByteArray, offset: Int, size: Int) :
    InputStream() {
    override fun read(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
