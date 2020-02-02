package com.soywiz.luak.compat.java.io

expect abstract class OutputStream() : Closeable {
    abstract fun write(value: Int)
    open fun write(b: ByteArray)
    open fun write(b: ByteArray, i: Int, size: Int)
    open fun flush()
}


expect open class FilterOutputStream(out: OutputStream) : OutputStream {
}

expect open class DataOutputStream(out: OutputStream) : FilterOutputStream {
    fun writeByte(toInt: Int): Unit
    fun writeLong(l: Long): Unit
    fun writeInt(x: Int): Unit
}

expect open class ByteArrayOutputStream() : OutputStream {
    open fun size(): Int
    fun toByteArray(): ByteArray
}

expect open class PrintStream constructor(os: OutputStream): FilterOutputStream, Appendable, Closeable {
    fun print(toChar: Char)
    fun print(a: Int)
    fun print(toChar: String)
    fun print(varargs: Any)
    fun println()
    fun println(s: String)
    fun println(varargs: Any)
}
