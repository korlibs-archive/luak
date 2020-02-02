package com.soywiz.luak.compat.java.lang

actual typealias Thread = java.lang.Thread

actual fun Object_notify(obj: Any) {
    (obj as java.lang.Object).notify()
}

actual fun Object_wait(obj: Any) {
    (obj as java.lang.Object).wait()
}

actual fun Object_wait(obj: Any, time: Long) {
    (obj as java.lang.Object).wait(time)
}

actual typealias InterruptedException = java.lang.InterruptedException
