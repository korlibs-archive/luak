@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*
import kotlin.system.*

internal actual object JSystem {
    actual val out: LuaWriter get() = System.out.toLua().toWriter()
    actual val err: LuaWriter get() = System.err.toLua().toWriter()
    actual val `in`: LuaBinInput get() = System.`in`.toLua()

    actual fun exit(code: Int): Unit = exitProcess(code)
    actual fun getProperty(key: String, def: String?): String? = System.getProperty(key, def)
    actual fun gc() = System.gc()
    actual fun totalMemory(): Long = Runtime.getRuntime().totalMemory()
    actual fun freeMemory(): Long = Runtime.getRuntime().freeMemory()
}

actual typealias Class<T> = java.lang.Class<T>
actual fun Class_forName(name: String): Class<*>? = java.lang.Class.forName(name)

actual typealias ReflectiveOperationException = java.lang.ReflectiveOperationException
actual typealias ClassNotFoundException = java.lang.ClassNotFoundException

actual typealias Closeable = java.io.Closeable

internal actual val KClass<*>.portableName: String get() = this.java.name
internal actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.java.isAssignableFrom(ins::class.java)
internal actual fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput? = this.java.getResourceAsStream(res)?.toLua()

actual typealias IOException = java.io.IOException
actual typealias EOFException = java.io.EOFException

actual typealias NativeThread = java.lang.Thread

actual fun NativeThread(runnable: () -> Unit, name: String): NativeThread = NativeThread(Runnable(runnable), name)

actual fun Object_notify(obj: Any) = (obj as Object).notify()
actual fun Object_wait(obj: Any) = (obj as Object).wait()
actual fun Object_wait(obj: Any, time: Long) = (obj as Object).wait(time)

actual typealias InterruptedException = java.lang.InterruptedException

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>
