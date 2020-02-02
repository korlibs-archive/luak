package com.soywiz.luak.compat.java.lang

import com.soywiz.luak.compat.java.io.*

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
    actual val `in`: InputStream get() = TODO()

    actual fun exit(code: Int) {
        TODO("exit($code)")
    }

    actual fun arraycopy(src: Any, srcPos: Int, dst: Any, dstPos: Int, count: Int) {
        @Suppress("UNCHECKED_CAST")
        when (src) {
            is BooleanArray -> src.copyInto(dst as BooleanArray, dstPos, srcPos, srcPos + count)
            is ByteArray -> src.copyInto(dst as ByteArray, dstPos, srcPos, srcPos + count)
            is CharArray -> src.copyInto(dst as CharArray, dstPos, srcPos, srcPos + count)
            is ShortArray -> src.copyInto(dst as ShortArray, dstPos, srcPos, srcPos + count)
            is IntArray -> src.copyInto(dst as IntArray, dstPos, srcPos, srcPos + count)
            is LongArray -> src.copyInto(dst as LongArray, dstPos, srcPos, srcPos + count)
            is FloatArray -> src.copyInto(dst as FloatArray, dstPos, srcPos, srcPos + count)
            is DoubleArray -> src.copyInto(dst as DoubleArray, dstPos, srcPos, srcPos + count)
            is Array<*> -> (src as Array<Any>).copyInto(dst as Array<Any>, dstPos, srcPos, srcPos + count)
        }
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
