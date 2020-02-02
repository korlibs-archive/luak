package com.soywiz.luak.compat.java.lang

import kotlin.jvm.*

class Integer {
    companion object {
        @JvmStatic
        fun toString(v: Int, radix: Int = 10) = v.toString(radix)
        @JvmStatic
        fun toHexString(v: Int) = v.toString(16)
    }
}

class Character {
    companion object {
        @JvmStatic
        fun isDigit(c: Char): Boolean = c in '0'..'9'
        fun toLowerCase(c: Char): Char = c.toLowerCase()
        fun isLowerCase(c: Char): Boolean = c.toLowerCase() == c
        fun isUpperCase(c: Char): Boolean = c.toUpperCase() == c
    }
}
