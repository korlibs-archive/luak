package com.soywiz.luak.compat.java

import com.soywiz.luak.compat.java.io.*
import kotlin.reflect.*

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
expect annotation class Throws(vararg val exceptionClasses: KClass<out Throwable>)

expect val KClass<*>.name: String
expect fun KClass<*>.isAssignableFrom(other: KClass<*>): Boolean
expect fun KClass<*>.getResourceAsStream(res: String): InputStream?
