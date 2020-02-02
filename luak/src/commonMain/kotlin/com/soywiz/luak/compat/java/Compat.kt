package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import org.luaj.vm2.io.*
import kotlin.reflect.*

expect val KClass<*>.portableName: String
expect fun KClass<*>.isInstancePortable(ins: Any): Boolean
expect fun KClass<*>.getResourceAsStreamPortable(res: String): LuaBinInput?
