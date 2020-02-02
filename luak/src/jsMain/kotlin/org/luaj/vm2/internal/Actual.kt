package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*

internal actual object JSystem {
    actual val out: PrintStream by lazy {
        PrintStream(object : OutputStream() {
            override fun write(value: Int) {
                print(value.toChar())
            }

            override fun close() {
            }
        })
    }
    actual val err: PrintStream get() = out
    actual val `in`: LuaBinInput get() = TODO()

    actual fun exit(code: Int) {
        TODO("exit($code)")
    }

    actual fun getProperty(key: String, def: String?): String? = when (key) {
        "CALLS" -> "0"
        "TRACE" -> "0"
        "luaj.package.path" -> "./"
        "file.separator" -> "/"
        "line.separator" -> "\n"
        else -> def
    }

    actual fun gc() = Unit

    actual fun totalMemory(): Long = 0L
    actual fun freeMemory(): Long = 0L
}

actual class Class<T> {
    actual fun newInstance(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun Class_forName(name: String): Class<*>? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual open class ReflectiveOperationException() : Exception()
actual open class ClassNotFoundException : ReflectiveOperationException()

actual interface Closeable {
    actual fun close()
}

internal actual val KClass<*>.portableName: String get() = this.simpleName ?: "Unknown"
internal actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.isInstance(ins)
internal actual fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput? = TODO("getResourceAsStream")

actual open class IOException : Exception {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

actual open class EOFException : IOException {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

actual class NativeThread {
    actual fun start() {
        TODO()
    }
}

actual fun NativeThread(runnable: () -> Unit, name: String): NativeThread = NativeThread()

actual fun Object_notify(obj: Any) {
    TODO()
}

actual fun Object_wait(obj: Any) {
    TODO()
}

actual fun Object_wait(obj: Any, time: Long) {
    TODO()
}

actual class InterruptedException : Exception()

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

actual class WeakReference<T> actual constructor(val value: T) {
    actual fun get(): T? {
        println("Warning: WeakReference not fully implemented in this target")
        return value
    }
}

