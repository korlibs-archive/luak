package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

actual val KClass<*>.name: String
    get() = this.simpleName ?: "Unknown"
actual fun KClass<*>.isInstance2(ins: Any): Boolean = this.isInstance(ins)
actual fun KClass<*>.getResourceAsStream(res: String): InputStream? = TODO("getResourceAsStream")
