package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

actual val KClass<*>.portableName: String
    get() = this.simpleName ?: "Unknown"
actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.isInstance(ins)
actual fun KClass<*>.getResourceAsStreamPortable(res: String): InputStream? = TODO("getResourceAsStream")
