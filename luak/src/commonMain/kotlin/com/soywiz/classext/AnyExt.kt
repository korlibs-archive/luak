package com.soywiz.classext

import kotlinx.coroutines.*

fun Any.notify() = Unit

fun Any.wait() = Unit
fun Any.wait(time: Long) = Unit

class InterruptedException : RuntimeException()

class Thread(val runnable: Runnable, val name: String) {
    fun start() {
        runnable.run()
    }
}
