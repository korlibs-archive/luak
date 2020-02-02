package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*

internal actual val KClass<*>.portableName: String get() = this.simpleName ?: "Unknown"
internal actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.isInstance(ins)
internal actual fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput? = TODO("getResourceAsStream")
