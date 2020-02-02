package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
actual annotation class Throws actual constructor(actual vararg val exceptionClasses: KClass<out Throwable>)

actual val KClass<*>.name: String
    get() = this.simpleName ?: "Unknown"
actual fun KClass<*>.isInstance2(ins: Any): Boolean = this.isInstance(ins)
actual fun KClass<*>.getResourceAsStream(res: String): InputStream? = TODO("getResourceAsStream")
