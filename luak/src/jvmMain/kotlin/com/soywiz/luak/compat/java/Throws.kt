package com.soywiz.luak.compat.java

import org.luaj.vm2.io.*
import java.io.*
import kotlin.reflect.*

actual val KClass<*>.portableName: String get() = this.java.name
actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.java.isAssignableFrom(ins::class.java)
actual fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput? = this.java.getResourceAsStream(res)?.toLua()
