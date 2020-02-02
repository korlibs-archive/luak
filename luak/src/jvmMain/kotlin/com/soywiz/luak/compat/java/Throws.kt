package com.soywiz.luak.compat.java

import java.io.*
import kotlin.reflect.*

actual typealias Throws = kotlin.jvm.Throws

actual val KClass<*>.name: String get() = this.java.name
actual fun KClass<*>.isInstance2(ins: Any): Boolean = this.java.isAssignableFrom(ins::class.java)
actual fun KClass<*>.getResourceAsStream(res: String): InputStream? = this.java.getResourceAsStream(res)
