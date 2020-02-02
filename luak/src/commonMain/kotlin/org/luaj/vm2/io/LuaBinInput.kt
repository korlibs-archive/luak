package org.luaj.vm2.io

abstract class LuaBinInput {
    abstract fun readByte(): Int
}

open class BytesLuaBinInput(val buf: ByteArray, val start: Int, val size: Int) : LuaBinInput() {
    var offset = 0
    override fun readByte(): Int = if (offset < size) buf[start + offset++].toInt() and 0xFF else -1
}
