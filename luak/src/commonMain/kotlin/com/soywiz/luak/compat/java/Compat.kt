package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

expect val KClass<*>.name: String
expect fun KClass<*>.isInstance2(ins: Any): Boolean
expect fun KClass<*>.getResourceAsStream(res: String): InputStream?
