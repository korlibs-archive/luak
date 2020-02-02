package org.luaj.vm2.internal

expect class Class<T> {
    fun newInstance(): T
}

expect fun Class_forName(name: String): Class<*>?

expect class ReflectiveOperationException : Exception
expect class ClassNotFoundException : ReflectiveOperationException
