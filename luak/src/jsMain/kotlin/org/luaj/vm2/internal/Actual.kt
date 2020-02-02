package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*

internal actual object JSystem {
    actual val out: LuaWriter by lazy {
        object : LuaWriter() {
            override fun print(v: String) = kotlin.io.println(v)
            override fun write(value: Int) = kotlin.io.print(value.toChar())
        }
    }
    actual val err: LuaWriter get() = out
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

actual class WeakReference<T> actual constructor(val value: T) {
    actual fun get(): T? {
        println("Warning: WeakReference not fully implemented in this target")
        return value
    }
}

