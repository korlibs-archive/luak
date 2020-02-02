package org.luaj.vm2.io

import java.io.*

fun Reader.toLuaReader(): LuaReader {
    val self = this
    return object : LuaReader() {
        override fun read(): Int = self.read()
        override fun read(cbuf: CharArray, off: Int, len: Int): Int = self.read(cbuf, off, len)
        override fun close() = self.close()
    }
}
