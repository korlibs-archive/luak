package com.soywiz.luak.compat.java.lang

expect class Thread(runnable: Runnable, name: String) {
    fun start()
}

expect fun Object_notify(obj: Any)
expect fun Object_wait(obj: Any)
expect fun Object_wait(obj: Any, time: Long)
