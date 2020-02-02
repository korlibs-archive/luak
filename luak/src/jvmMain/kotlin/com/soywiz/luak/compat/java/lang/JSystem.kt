package com.soywiz.luak.compat.java.lang

import com.soywiz.luak.compat.java.io.*
import kotlin.system.*

actual object JSystem {
    actual val out: PrintStream get() = System.out
    actual val err: PrintStream get() = System.err
    actual val `in`: InputStream get() = System.`in`

    actual fun exit(code: Int): Unit = exitProcess(code)
    actual fun arraycopy(src: Any, srcPos: Int, dst: Any, dstPos: Int, count: Int) = System.arraycopy(src, srcPos, dst, dstPos, count)
    actual fun getProperty(key: String, def: String?): String? = System.getProperty(key, def)
    actual fun gc() = System.gc()
    actual fun totalMemory(): Long = Runtime.getRuntime().totalMemory()
    actual fun freeMemory(): Long = Runtime.getRuntime().freeMemory()
}
