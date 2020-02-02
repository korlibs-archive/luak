package com.soywiz.luak.compat.java.lang

actual typealias Class<T> = java.lang.Class<T>
actual fun Class_forName(name: String): Class<*>? = java.lang.Class.forName(name)

actual typealias ReflectiveOperationException = java.lang.ReflectiveOperationException
actual typealias ClassNotFoundException = java.lang.ClassNotFoundException
