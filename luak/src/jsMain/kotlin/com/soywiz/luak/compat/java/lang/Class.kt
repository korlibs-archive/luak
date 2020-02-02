package com.soywiz.luak.compat.java.lang

actual class Class<T> {
    actual fun newInstance(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual fun Class_forName(name: String): Class<*>? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual open class ReflectiveOperationException() : Exception()
actual open class ClassNotFoundException : ReflectiveOperationException()
