package org.luaj.vm2.compiler

import junit.framework.TestCase

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaDouble
import org.luaj.vm2.LuaInteger
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

class SimpleTests : TestCase() {

    private var globals: Globals? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        globals = JsePlatform.standardGlobals()
    }

    private fun doTest(script: String) {
        try {
            val c = globals!!.load(script, "script")
            c.call()
        } catch (e: Exception) {
            TestCase.fail("i/o exception: $e")
        }

    }

    fun testTrivial() {
        val s = "print( 2 )\n"
        doTest(s)
    }

    fun testAlmostTrivial() {
        val s = "print( 2 )\n" + "print( 3 )\n"
        doTest(s)
    }

    fun testSimple() {
        val s = "print( 'hello, world' )\n" +
                "for i = 2,4 do\n" +
                "	print( 'i', i )\n" +
                "end\n"
        doTest(s)
    }

    fun testBreak() {
        val s = "a=1\n" +
                "while true do\n" +
                "  if a>10 then\n" +
                "     break\n" +
                "  end\n" +
                "  a=a+1\n" +
                "  print( a )\n" +
                "end\n"
        doTest(s)
    }

    fun testShebang() {
        val s = "#!../lua\n" + "print( 2 )\n"
        doTest(s)
    }

    fun testInlineTable() {
        val s = "A = {g=10}\n" + "print( A )\n"
        doTest(s)
    }

    fun testEqualsAnd() {
        val s = "print( 1 == b and b )\n"
        doTest(s)
    }

    fun testDoubleHashCode() {
        for (i in samehash.indices) {
            val j = LuaInteger.valueOf(samehash[i])
            val d = LuaDouble.valueOf(samehash[i].toDouble())
            val hj = j.hashCode()
            val hd = d.hashCode()
            TestCase.assertEquals(hj, hd)
        }
        var i = 0
        while (i < diffhash.size) {
            val c = LuaValue.valueOf(diffhash[i + 0])
            val d = LuaValue.valueOf(diffhash[i + 1])
            val hc = c.hashCode()
            val hd = d.hashCode()
            TestCase.assertTrue("hash codes are same: $hc", hc != hd)
            i += 2
        }
    }

    companion object {

        private val samehash = intArrayOf(0, 1, -1, 2, -2, 4, 8, 16, 32, Integer.MAX_VALUE, Integer.MIN_VALUE)
        private val diffhash = doubleArrayOf(.5, 1.0, 1.5, 1.0, .5, 1.5, 1.25, 2.5)
    }
}
