package org.luaj.vm2.internal

expect class WeakReference<T>(value: T) {
    fun get(): T?
}
