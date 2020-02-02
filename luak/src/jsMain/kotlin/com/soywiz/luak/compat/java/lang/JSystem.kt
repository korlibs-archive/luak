package com.soywiz.luak.compat.java.lang

import com.soywiz.luak.compat.java.io.*

actual object JSystem {
    actual val out: PrintStream
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val err: PrintStream
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    actual val `in`: InputStream
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    actual fun exit(code: Int) {
    }

    actual fun arraycopy(src: Any, srcPos: Int, dst: Any, dstPos: Int, count: Int) {
    }

    actual fun getProperty(key: String, def: String?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun gc() {
    }

    actual fun currentTimeMillis(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun totalMemory(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun freeMemory(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
