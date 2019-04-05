package com.soywiz.classext

import com.soywiz.korio.lang.*

object System {
    val `in`: InputStream get() = TODO()
    val out: PrintStream get() = TODO()
    val err: PrintStream get() = TODO()
}

open class OutputStream : Closeable {
    open fun write(v: Int) {
    }

    open fun write(data: ByteArray, offset: Int = 0, size: Int = data.size - offset) {
        for (n in offset until offset + size) {
            write(data[n].toInt())
        }
    }

    open fun flush() {
    }

    override fun close() {
    }
}

open class InputStream : Closeable {
    open fun read(): Int = -1
    open fun read(data: ByteArray, offset: Int = 0, size: Int = data.size - offset): Int = -1

    override fun close() {
    }
}

open class PrintStream(val output: OutputStream) : Closeable {
    val EOL = "\n"

    fun print(v: Char): Unit = TODO()
    fun print(v: Int): Unit = TODO()
    fun print(v: String): Unit = TODO()
    fun print(v: Any): Unit = TODO()
    fun println(): Unit = print(EOL)
    fun println(v: String): Unit = run { print(v) }.also { print(EOL) }
    fun println(v: Any): Unit = run { print(v) }.also { print(EOL) }

    open fun flush() {
        output.flush()
    }

    override fun close() {
        output.close()
    }
}

open class ByteArrayOutputStream() : OutputStream() {

}

open class ByteArrayInputStream(val b: ByteArray, var offset: Int, var length: Int) : InputStream() {

}

open class DataOutputStream : OutputStream() {
}

open class DataInputStream : InputStream() {
}

open class Reader : Closeable {
    override fun close() {
    }
}
