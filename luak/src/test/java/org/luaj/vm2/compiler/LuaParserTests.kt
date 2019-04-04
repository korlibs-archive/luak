package org.luaj.vm2.compiler

import junit.framework.TestCase
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

import org.luaj.vm2.LuaValue
import org.luaj.vm2.parser.LuaParser

class LuaParserTests : CompilerUnitTests() {

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        LuaValue.valueOf(true)
    }

    override fun doTest(file: String) {
        try {
            val `is` = inputStreamOfFile(file)
            val r = InputStreamReader(`is`, "ISO-8859-1")
            val parser = LuaParser(r)
            parser.Chunk()
        } catch (e: Exception) {
            TestCase.fail(e.message)
            e.printStackTrace()
        }

    }
}
