package com.soywiz.luak.compat.java.lang

actual class Thread actual constructor(runnable: Runnable, name: String) {
    actual fun start() {
        TODO()
    }
}

actual fun Object_notify(obj: Any) {
    TODO()
}

actual fun Object_wait(obj: Any) {
    TODO()
}

actual fun Object_wait(obj: Any, time: Long) {
    TODO()
}

actual class InterruptedException : Exception()
