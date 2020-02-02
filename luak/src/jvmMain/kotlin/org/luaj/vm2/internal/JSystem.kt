package org.luaj.vm2.internal

import com.soywiz.luak.compat.java.io.*
import org.luaj.vm2.io.*
import kotlin.system.*

internal actual object JSystem {
    actual val out: PrintStream get() = System.out
    actual val err: PrintStream get() = System.err
    actual val `in`: LuaBinInput get() = System.`in`.toLua()

    actual fun exit(code: Int): Unit = exitProcess(code)
    actual fun getProperty(key: String, def: String?): String? = System.getProperty(key, def)
    actual fun gc() = System.gc()
    actual fun totalMemory(): Long = Runtime.getRuntime().totalMemory()
    actual fun freeMemory(): Long = Runtime.getRuntime().freeMemory()
}
