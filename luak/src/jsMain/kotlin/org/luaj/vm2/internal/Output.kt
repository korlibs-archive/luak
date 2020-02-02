package org.luaj.vm2.internal

import org.luaj.vm2.internal.Closeable

actual abstract class OutputStream actual constructor() : Closeable {
    actual abstract fun write(value: Int)
    actual open fun write(b: ByteArray): Unit = TODO()
    actual open fun write(b: ByteArray, i: Int, size: Int): Unit = TODO()
    actual open fun flush() = Unit
}

actual open class FilterOutputStream actual constructor(val out: OutputStream) : OutputStream() {
    override fun write(value: Int) {
        out.write(value)
    }

    override fun close() {
        out.close()
    }
}

actual open class DataOutputStream actual constructor(out: OutputStream) : FilterOutputStream(out) {
    actual fun writeByte(toInt: Int) {
    }

    actual fun writeLong(l: Long) {
    }

    actual fun writeInt(x: Int) {
    }
}

actual open class ByteArrayOutputStream : OutputStream() {
    actual open fun size(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun toByteArray(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual open class PrintStream actual constructor(os: OutputStream) : FilterOutputStream(os),
    Appendable, Closeable {
    actual fun print(toChar: Char) {
    }

    actual fun print(a: Int) {
    }

    actual fun print(toChar: String) {
    }

    actual fun print(varargs: Any) {
    }

    actual fun println() {
    }

    actual fun println(s: String) {
    }

    actual fun println(varargs: Any) {
    }

    override fun append(c: Char): Appendable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun append(csq: CharSequence?): Appendable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): Appendable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
