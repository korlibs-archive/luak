package org.luaj.vm2.internal

import org.luaj.vm2.io.*
import kotlin.reflect.*

internal expect val KClass<*>.portableName: String
internal expect fun KClass<*>.isInstancePortable(ins: Any): Boolean
internal expect fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput?
