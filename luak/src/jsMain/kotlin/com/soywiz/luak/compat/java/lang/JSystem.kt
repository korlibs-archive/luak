package com.soywiz.luak.compat.java.lang

import com.soywiz.luak.compat.java.io.*
import org.luaj.vm2.io.*

actual object JSystem {
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
