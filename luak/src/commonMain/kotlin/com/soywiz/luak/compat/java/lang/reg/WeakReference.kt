package com.soywiz.luak.compat.java.lang.reg

expect class WeakReference<T>(value: T) {
    fun get(): T?
}
