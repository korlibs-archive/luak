package com.soywiz.luak.compat.java

import java.io.*
import kotlin.reflect.*

actual val KClass<*>.portableName: String get() = this.java.name
actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.java.isAssignableFrom(ins::class.java)
actual fun KClass<*>.getResourceAsStreamPortable(res: String): InputStream? = this.java.getResourceAsStream(res)
