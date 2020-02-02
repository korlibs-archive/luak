package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import java.io.*
import kotlin.reflect.*

internal actual val KClass<*>.portableName: String get() = this.java.name
internal actual fun KClass<*>.isInstancePortable(ins: Any): Boolean = this.java.isAssignableFrom(ins::class.java)
internal actual fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput? = this.java.getResourceAsStream(res)?.toLua()
