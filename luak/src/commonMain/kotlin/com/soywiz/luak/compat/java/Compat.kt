package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

expect val KClass<*>.portableName: String
expect fun KClass<*>.isInstancePortable(ins: Any): Boolean
expect fun KClass<*>.getResourceAsStreamPortable(res: String): InputStream?
