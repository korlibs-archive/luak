package com.soywiz.luak.compat.java.lang

import com.soywiz.luak.compat.java.io.*

expect object JSystem {
    val out: PrintStream
    val err: PrintStream
    val `in`: InputStream
    fun exit(code: Int)
    fun arraycopy(src: Any, srcPos: Int, dst: Any, dstPos: Int, count: Int)
    fun getProperty(key: String, def: String? = null): String?
    fun gc()
    fun currentTimeMillis(): Long
    fun totalMemory(): Long
    fun freeMemory(): Long
}
