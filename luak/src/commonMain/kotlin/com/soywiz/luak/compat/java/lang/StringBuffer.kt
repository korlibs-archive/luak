package com.soywiz.luak.compat.java.lang

typealias StringBuffer = StringBuilder

fun StringBuffer.setLength(size: Int) {
    if (size == 0) {
        clear()
    } else {
        TODO()
    }
}
