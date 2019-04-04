/*******************************************************************************
 * Copyright (c) 2012 Luaj.org. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.luaj.vm2

import java.lang.ref.WeakReference

import junit.framework.TestCase

import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.jse.JsePlatform


class OrphanedThreadTest : TestCase() {

    internal lateinit var globals: Globals
    internal var luathread: LuaThread? = null
    internal lateinit var luathr_ref: WeakReference<*>
    internal var function: LuaValue? = null
    internal lateinit var func_ref: WeakReference<*>

    @Throws(Exception::class)
    override fun setUp() {
        LuaThread.thread_orphan_check_interval = 5
        globals = JsePlatform.standardGlobals()
    }

    override fun tearDown() {
        LuaThread.thread_orphan_check_interval = 30000
    }

    @Throws(Exception::class)
    fun testCollectOrphanedNormalThread() {
        function = NormalFunction(globals)
        doTest(LuaValue.TRUE, LuaValue.ZERO)
    }

    @Throws(Exception::class)
    fun testCollectOrphanedEarlyCompletionThread() {
        function = EarlyCompletionFunction(globals)
        doTest(LuaValue.TRUE, LuaValue.ZERO)
    }

    @Throws(Exception::class)
    fun testCollectOrphanedAbnormalThread() {
        function = AbnormalFunction(globals)
        doTest(LuaValue.FALSE, LuaValue.valueOf("abnormal condition"))
    }

    @Throws(Exception::class)
    fun testCollectOrphanedClosureThread() {
        val script = "print('in closure, arg is '..(...))\n" +
                "arg = coroutine.yield(1)\n" +
                "print('in closure.2, arg is '..arg)\n" +
                "arg = coroutine.yield(0)\n" +
                "print('leakage in closure.3, arg is '..arg)\n" +
                "return 'done'\n"
        function = globals.load(script, "script")
        doTest(LuaValue.TRUE, LuaValue.ZERO)
    }

    @Throws(Exception::class)
    fun testCollectOrphanedPcallClosureThread() {
        val script = "f = function(x)\n" +
                "  print('in pcall-closure, arg is '..(x))\n" +
                "  arg = coroutine.yield(1)\n" +
                "  print('in pcall-closure.2, arg is '..arg)\n" +
                "  arg = coroutine.yield(0)\n" +
                "  print('leakage in pcall-closure.3, arg is '..arg)\n" +
                "  return 'done'\n" +
                "end\n" +
                "print( 'pcall-closre.result:', pcall( f, ... ) )\n"
        function = globals.load(script, "script")
        doTest(LuaValue.TRUE, LuaValue.ZERO)
    }

    @Throws(Exception::class)
    fun testCollectOrphanedLoadCloasureThread() {
        val script = "t = { \"print \", \"'hello, \", \"world'\", }\n" +
                "i = 0\n" +
                "arg = ...\n" +
                "f = function()\n" +
                "	i = i + 1\n" +
                "   print('in load-closure, arg is', arg, 'next is', t[i])\n" +
                "   arg = coroutine.yield(1)\n" +
                "	return t[i]\n" +
                "end\n" +
                "load(f)()\n"
        function = globals.load(script, "script")
        doTest(LuaValue.TRUE, LuaValue.ONE)
    }

    @Throws(Exception::class)
    private fun doTest(status2: LuaValue, value2: LuaValue) {
        luathread = LuaThread(globals, function)
        luathr_ref = WeakReference(luathread)
        func_ref = WeakReference(function)
        TestCase.assertNotNull(luathr_ref.get())

        // resume two times
        var a = luathread!!.resume(LuaValue.valueOf("foo"))
        TestCase.assertEquals(LuaValue.ONE, a.arg(2))
        TestCase.assertEquals(LuaValue.TRUE, a.arg1())
        a = luathread!!.resume(LuaValue.valueOf("bar"))
        TestCase.assertEquals(value2, a.arg(2))
        TestCase.assertEquals(status2, a.arg1())

        // drop strong references
        luathread = null
        function = null

        // gc
        var i = 0
        while (i < 100 && (luathr_ref.get() != null || func_ref.get() != null)) {
            Runtime.getRuntime().gc()
            Thread.sleep(5)
            i++
        }

        // check reference
        TestCase.assertNull(luathr_ref.get())
        TestCase.assertNull(func_ref.get())
    }


    internal class NormalFunction(val globals: Globals) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            var arg = arg
            println("in normal.1, arg is $arg")
            arg = globals.yield(LuaValue.ONE).arg1()
            println("in normal.2, arg is $arg")
            arg = globals.yield(LuaValue.ZERO).arg1()
            println("in normal.3, arg is $arg")
            return LuaValue.NONE
        }
    }

    internal class EarlyCompletionFunction(val globals: Globals) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            var arg = arg
            println("in early.1, arg is $arg")
            arg = globals.yield(LuaValue.ONE).arg1()
            println("in early.2, arg is $arg")
            return LuaValue.ZERO
        }
    }

    internal class AbnormalFunction(val globals: Globals) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            var arg = arg
            println("in abnormal.1, arg is $arg")
            arg = globals.yield(LuaValue.ONE).arg1()
            println("in abnormal.2, arg is $arg")
            LuaValue.error("abnormal condition")
            return LuaValue.ZERO
        }
    }
}
