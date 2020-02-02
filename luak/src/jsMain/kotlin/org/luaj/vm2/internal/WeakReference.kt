package org.luaj.vm2.internal

actual class WeakReference<T> actual constructor(val value: T) {
    actual fun get(): T? {
        println("Warning: WeakReference not fully implemented in this target")
        return value
    }
}
