package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*

internal expect object JSystem {
    val out: LuaWriter
    val err: LuaWriter
    val `in`: LuaBinInput
    fun exit(code: Int)
    fun getProperty(key: String, def: String? = null): String?
    fun gc()
    fun totalMemory(): Long
    fun freeMemory(): Long
}

expect class Class<T> {
    fun newInstance(): T
}

expect fun Class_forName(name: String): Class<*>?

expect class ReflectiveOperationException : Exception
expect class ClassNotFoundException : ReflectiveOperationException

expect open class IOException : Exception {
    constructor()
    constructor(message: String)
}

expect open class EOFException : IOException {
    constructor()
    constructor(message: String)
}

internal expect val KClass<*>.portableName: String
internal expect fun KClass<*>.isInstancePortable(ins: Any): Boolean
internal expect fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput?

expect interface Closeable {
    fun close()
}

expect class NativeThread {
    fun start()
}

expect fun NativeThread(runnable: () -> Unit, name: String): NativeThread

expect fun Object_notify(obj: Any)
expect fun Object_wait(obj: Any)
expect fun Object_wait(obj: Any, time: Long)

expect class InterruptedException : Exception

expect class WeakReference<T>(value: T) {
    fun get(): T?
}
