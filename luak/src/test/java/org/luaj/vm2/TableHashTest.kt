/*******************************************************************************
 * Copyright (c) 2009 Luaj.org. All rights reserved.
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

import junit.framework.TestCase

import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Tests for tables used as lists.
 */
class TableHashTest : TestCase() {

    protected fun new_Table(): LuaTable {
        return LuaTable()
    }

    protected fun new_Table(n: Int, m: Int): LuaTable {
        return LuaTable(n, m)
    }

    fun testSetRemove() {
        val t = new_Table()

        TestCase.assertEquals(0, t.hashLength)
        TestCase.assertEquals(0, t.length())
        TestCase.assertEquals(0, t.keyCount())

        val keys = arrayOf(
            "abc",
            "def",
            "ghi",
            "jkl",
            "mno",
            "pqr",
            "stu",
            "wxy",
            "z01",
            "cd",
            "ef",
            "g",
            "hi",
            "jk",
            "lm",
            "no",
            "pq",
            "rs"
        )
        val capacities = intArrayOf(0, 2, 2, 4, 4, 8, 8, 8, 8, 16, 16, 16, 16, 16, 16, 16, 16, 32, 32, 32)
        for (i in keys.indices) {
            TestCase.assertEquals(capacities[i], t.hashLength)
            val si = "Test Value! $i"
            t.set(keys[i], si)
            TestCase.assertEquals(0, t.length())
            TestCase.assertEquals(i + 1, t.keyCount())
        }
        TestCase.assertEquals(capacities[keys.size], t.hashLength)
        for (i in keys.indices) {
            val vi = LuaString.valueOf("Test Value! $i")
            TestCase.assertEquals(vi, t.get(keys[i]))
            TestCase.assertEquals(vi, t.get(LuaString.valueOf(keys[i])))
            TestCase.assertEquals(vi, t.rawget(keys[i]))
            TestCase.assertEquals(vi, t.rawget(keys[i]))
        }

        // replace with new values
        for (i in keys.indices) {
            t.set(keys[i], LuaString.valueOf("Replacement Value! $i"))
            TestCase.assertEquals(0, t.length())
            TestCase.assertEquals(keys.size, t.keyCount())
            TestCase.assertEquals(capacities[keys.size], t.hashLength)
        }
        for (i in keys.indices) {
            val vi = LuaString.valueOf("Replacement Value! $i")
            TestCase.assertEquals(vi, t.get(keys[i]))
        }

        // remove
        for (i in keys.indices) {
            t.set(keys[i], LuaValue.NIL)
            TestCase.assertEquals(0, t.length())
            TestCase.assertEquals(keys.size - i - 1, t.keyCount())
            if (i < keys.size - 1)
                TestCase.assertEquals(capacities[keys.size], t.hashLength)
            else
                TestCase.assertTrue(0 <= t.hashLength)
        }
        for (i in keys.indices) {
            TestCase.assertEquals(LuaValue.NIL, t.get(keys[i]))
        }
    }

    fun testIndexMetatag() {
        val t = new_Table()
        val mt = new_Table()
        val fb = new_Table()

        // set basic values
        t.set("ppp", "abc")
        t.set(123, "def")
        mt.set(LuaValue.INDEX, fb)
        fb.set("qqq", "ghi")
        fb.set(456, "jkl")

        // check before setting metatable
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("nil", t.get("qqq").tojstring())
        TestCase.assertEquals("nil", t.get(456).tojstring())
        TestCase.assertEquals("nil", fb.get("ppp").tojstring())
        TestCase.assertEquals("nil", fb.get(123).tojstring())
        TestCase.assertEquals("ghi", fb.get("qqq").tojstring())
        TestCase.assertEquals("jkl", fb.get(456).tojstring())
        TestCase.assertEquals("nil", mt.get("ppp").tojstring())
        TestCase.assertEquals("nil", mt.get(123).tojstring())
        TestCase.assertEquals("nil", mt.get("qqq").tojstring())
        TestCase.assertEquals("nil", mt.get(456).tojstring())

        // check before setting metatable
        t.setmetatable(mt)
        TestCase.assertEquals(mt, t.getmetatable())
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("ghi", t.get("qqq").tojstring())
        TestCase.assertEquals("jkl", t.get(456).tojstring())
        TestCase.assertEquals("nil", fb.get("ppp").tojstring())
        TestCase.assertEquals("nil", fb.get(123).tojstring())
        TestCase.assertEquals("ghi", fb.get("qqq").tojstring())
        TestCase.assertEquals("jkl", fb.get(456).tojstring())
        TestCase.assertEquals("nil", mt.get("ppp").tojstring())
        TestCase.assertEquals("nil", mt.get(123).tojstring())
        TestCase.assertEquals("nil", mt.get("qqq").tojstring())
        TestCase.assertEquals("nil", mt.get(456).tojstring())

        // set metatable to metatable without values
        t.setmetatable(fb)
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("nil", t.get("qqq").tojstring())
        TestCase.assertEquals("nil", t.get(456).tojstring())

        // set metatable to null
        t.setmetatable(null)
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("nil", t.get("qqq").tojstring())
        TestCase.assertEquals("nil", t.get(456).tojstring())
    }

    fun testIndexFunction() {
        val t = new_Table()
        val mt = new_Table()

        val fb = object : TwoArgFunction() {
            override fun call(tbl: LuaValue, key: LuaValue): LuaValue {
                TestCase.assertEquals(tbl, t)
                return LuaValue.valueOf("from mt: $key")
            }
        }

        // set basic values
        t.set("ppp", "abc")
        t.set(123, "def")
        mt.set(LuaValue.INDEX, fb)

        // check before setting metatable
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("nil", t.get("qqq").tojstring())
        TestCase.assertEquals("nil", t.get(456).tojstring())


        // check before setting metatable
        t.setmetatable(mt)
        TestCase.assertEquals(mt, t.getmetatable())
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("from mt: qqq", t.get("qqq").tojstring())
        TestCase.assertEquals("from mt: 456", t.get(456).tojstring())

        // use raw set
        t.rawset("qqq", "alt-qqq")
        t.rawset(456, "alt-456")
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("alt-qqq", t.get("qqq").tojstring())
        TestCase.assertEquals("alt-456", t.get(456).tojstring())

        // remove using raw set
        t.rawset("qqq", LuaValue.NIL)
        t.rawset(456, LuaValue.NIL)
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("from mt: qqq", t.get("qqq").tojstring())
        TestCase.assertEquals("from mt: 456", t.get(456).tojstring())

        // set metatable to null
        t.setmetatable(null)
        TestCase.assertEquals("abc", t.get("ppp").tojstring())
        TestCase.assertEquals("def", t.get(123).tojstring())
        TestCase.assertEquals("nil", t.get("qqq").tojstring())
        TestCase.assertEquals("nil", t.get(456).tojstring())
    }

    fun testNext() {
        val t = new_Table()
        TestCase.assertEquals(LuaValue.NIL, t.next(LuaValue.NIL))

        // insert array elements
        t.set(1, "one")
        TestCase.assertEquals(LuaValue.valueOf(1), t.next(LuaValue.NIL).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("one"), t.next(LuaValue.NIL).arg(2))
        TestCase.assertEquals(LuaValue.NIL, t.next(LuaValue.ONE))
        t.set(2, "two")
        TestCase.assertEquals(LuaValue.valueOf(1), t.next(LuaValue.NIL).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("one"), t.next(LuaValue.NIL).arg(2))
        TestCase.assertEquals(LuaValue.valueOf(2), t.next(LuaValue.ONE).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("two"), t.next(LuaValue.ONE).arg(2))
        TestCase.assertEquals(LuaValue.NIL, t.next(LuaValue.valueOf(2)))

        // insert hash elements
        t.set("aa", "aaa")
        TestCase.assertEquals(LuaValue.valueOf(1), t.next(LuaValue.NIL).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("one"), t.next(LuaValue.NIL).arg(2))
        TestCase.assertEquals(LuaValue.valueOf(2), t.next(LuaValue.ONE).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("two"), t.next(LuaValue.ONE).arg(2))
        TestCase.assertEquals(LuaValue.valueOf("aa"), t.next(LuaValue.valueOf(2)).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("aaa"), t.next(LuaValue.valueOf(2)).arg(2))
        TestCase.assertEquals(LuaValue.NIL, t.next(LuaValue.valueOf("aa")))
        t.set("bb", "bbb")
        TestCase.assertEquals(LuaValue.valueOf(1), t.next(LuaValue.NIL).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("one"), t.next(LuaValue.NIL).arg(2))
        TestCase.assertEquals(LuaValue.valueOf(2), t.next(LuaValue.ONE).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("two"), t.next(LuaValue.ONE).arg(2))
        TestCase.assertEquals(LuaValue.valueOf("aa"), t.next(LuaValue.valueOf(2)).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("aaa"), t.next(LuaValue.valueOf(2)).arg(2))
        TestCase.assertEquals(LuaValue.valueOf("bb"), t.next(LuaValue.valueOf("aa")).arg(1))
        TestCase.assertEquals(LuaValue.valueOf("bbb"), t.next(LuaValue.valueOf("aa")).arg(2))
        TestCase.assertEquals(LuaValue.NIL, t.next(LuaValue.valueOf("bb")))
    }

    fun testLoopWithRemoval() {
        val t = new_Table()

        t.set(LuaValue.valueOf(1), LuaValue.valueOf("1"))
        t.set(LuaValue.valueOf(3), LuaValue.valueOf("3"))
        t.set(LuaValue.valueOf(8), LuaValue.valueOf("4"))
        t.set(LuaValue.valueOf(17), LuaValue.valueOf("5"))
        t.set(LuaValue.valueOf(26), LuaValue.valueOf("6"))
        t.set(LuaValue.valueOf(35), LuaValue.valueOf("7"))
        t.set(LuaValue.valueOf(42), LuaValue.valueOf("8"))
        t.set(LuaValue.valueOf(60), LuaValue.valueOf("10"))
        t.set(LuaValue.valueOf(63), LuaValue.valueOf("11"))

        var entry = t.next(LuaValue.NIL)
        while (!entry.isnil(1)) {
            val k = entry.arg1()
            val v = entry.arg(2)
            if (k.toint() and 1 == 0) {
                t.set(k, LuaValue.NIL)
            }
            entry = t.next(k)
        }

        var numEntries = 0
        entry = t.next(LuaValue.NIL)
        while (!entry.isnil(1)) {
            val k = entry.arg1()
            // Only odd keys should remain
            TestCase.assertTrue(k.toint() and 1 == 1)
            numEntries++
            entry = t.next(k)
        }
        TestCase.assertEquals(5, numEntries)
    }

    fun testLoopWithRemovalAndSet() {
        val t = new_Table()

        t.set(LuaValue.valueOf(1), LuaValue.valueOf("1"))
        t.set(LuaValue.valueOf(3), LuaValue.valueOf("3"))
        t.set(LuaValue.valueOf(8), LuaValue.valueOf("4"))
        t.set(LuaValue.valueOf(17), LuaValue.valueOf("5"))
        t.set(LuaValue.valueOf(26), LuaValue.valueOf("6"))
        t.set(LuaValue.valueOf(35), LuaValue.valueOf("7"))
        t.set(LuaValue.valueOf(42), LuaValue.valueOf("8"))
        t.set(LuaValue.valueOf(60), LuaValue.valueOf("10"))
        t.set(LuaValue.valueOf(63), LuaValue.valueOf("11"))

        var entry = t.next(LuaValue.NIL)
        var entry2 = entry
        while (!entry.isnil(1)) {
            val k = entry.arg1()
            val v = entry.arg(2)
            if (k.toint() and 1 == 0) {
                t.set(k, LuaValue.NIL)
            } else {
                t.set(k, v.tonumber())
                entry2 = t.next(entry2.arg1())
            }
            entry = t.next(k)
        }

        var numEntries = 0
        entry = t.next(LuaValue.NIL)
        while (!entry.isnil(1)) {
            val k = entry.arg1()
            // Only odd keys should remain
            TestCase.assertTrue(k.toint() and 1 == 1)
            TestCase.assertTrue(entry.arg(2).type() == LuaValue.TNUMBER)
            numEntries++
            entry = t.next(k)
        }
        TestCase.assertEquals(5, numEntries)
    }
}
