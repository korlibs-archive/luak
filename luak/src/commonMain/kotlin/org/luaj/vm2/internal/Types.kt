package org.luaj.vm2.internal

internal fun Int.toHexString() = toString(16)
internal fun Char.isDigit() = this in '0'..'9'
internal fun Char.isLowerCase() = this.toLowerCase() == this
internal fun Char.isUpperCase() = this.toUpperCase() == this
