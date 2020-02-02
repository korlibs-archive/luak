package com.soywiz.luak.compat.java.lang

expect class WeakReference<T>(value: T) {
    fun get(): T?
}
