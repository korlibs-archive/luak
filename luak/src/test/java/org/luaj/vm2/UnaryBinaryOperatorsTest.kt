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

import java.lang.reflect.InvocationTargetException

import junit.framework.TestCase

import org.luaj.vm2.lib.TwoArgFunction

/**
 * Tests of basic unary and binary operators on main value types.
 */
class UnaryBinaryOperatorsTest : TestCase() {

    internal lateinit var dummy: LuaValue

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        dummy = LuaValue.ZERO
    }

    fun testEqualsBool() {
        TestCase.assertEquals(LuaValue.FALSE, LuaValue.FALSE)
        TestCase.assertEquals(LuaValue.TRUE, LuaValue.TRUE)
        TestCase.assertTrue(LuaValue.FALSE == LuaValue.FALSE)
        TestCase.assertTrue(LuaValue.TRUE == LuaValue.TRUE)
        TestCase.assertTrue(LuaValue.FALSE != LuaValue.TRUE)
        TestCase.assertTrue(LuaValue.TRUE != LuaValue.FALSE)
        TestCase.assertTrue(LuaValue.FALSE.eq_b(LuaValue.FALSE))
        TestCase.assertTrue(LuaValue.TRUE.eq_b(LuaValue.TRUE))
        TestCase.assertFalse(LuaValue.FALSE.eq_b(LuaValue.TRUE))
        TestCase.assertFalse(LuaValue.TRUE.eq_b(LuaValue.FALSE))
        TestCase.assertEquals(LuaValue.TRUE, LuaValue.FALSE.eq(LuaValue.FALSE))
        TestCase.assertEquals(LuaValue.TRUE, LuaValue.TRUE.eq(LuaValue.TRUE))
        TestCase.assertEquals(LuaValue.FALSE, LuaValue.FALSE.eq(LuaValue.TRUE))
        TestCase.assertEquals(LuaValue.FALSE, LuaValue.TRUE.eq(LuaValue.FALSE))
        TestCase.assertFalse(LuaValue.FALSE.neq_b(LuaValue.FALSE))
        TestCase.assertFalse(LuaValue.TRUE.neq_b(LuaValue.TRUE))
        TestCase.assertTrue(LuaValue.FALSE.neq_b(LuaValue.TRUE))
        TestCase.assertTrue(LuaValue.TRUE.neq_b(LuaValue.FALSE))
        TestCase.assertEquals(LuaValue.FALSE, LuaValue.FALSE.neq(LuaValue.FALSE))
        TestCase.assertEquals(LuaValue.FALSE, LuaValue.TRUE.neq(LuaValue.TRUE))
        TestCase.assertEquals(LuaValue.TRUE, LuaValue.FALSE.neq(LuaValue.TRUE))
        TestCase.assertEquals(LuaValue.TRUE, LuaValue.TRUE.neq(LuaValue.FALSE))
        TestCase.assertTrue(LuaValue.TRUE.toboolean())
        TestCase.assertFalse(LuaValue.FALSE.toboolean())
    }

    fun testNot() {
        val ia = LuaValue.valueOf(3)
        val da = LuaValue.valueOf(.25)
        val sa = LuaValue.valueOf("1.5")
        val ba = LuaValue.TRUE
        val bb = LuaValue.FALSE

        // like kinds
        TestCase.assertEquals(LuaValue.FALSE, ia.not())
        TestCase.assertEquals(LuaValue.FALSE, da.not())
        TestCase.assertEquals(LuaValue.FALSE, sa.not())
        TestCase.assertEquals(LuaValue.FALSE, ba.not())
        TestCase.assertEquals(LuaValue.TRUE, bb.not())
    }

    fun testNeg() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(-4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(-.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("-2.0")

        // like kinds
        TestCase.assertEquals(-3.0, ia.neg().todouble())
        TestCase.assertEquals(-.25, da.neg().todouble())
        TestCase.assertEquals(-1.5, sa.neg().todouble())
        TestCase.assertEquals(4.0, ib.neg().todouble())
        TestCase.assertEquals(.5, db.neg().todouble())
        TestCase.assertEquals(2.0, sb.neg().todouble())
    }

    fun testDoublesBecomeInts() {
        // DoubleValue.valueOf should return int
        val ia = LuaInteger.valueOf(345)
        val da = LuaDouble.valueOf(345.0)
        val db = LuaDouble.valueOf(345.5)
        val sa = LuaValue.valueOf("3.0")
        val sb = LuaValue.valueOf("3")
        val sc = LuaValue.valueOf("-2.0")
        val sd = LuaValue.valueOf("-2")

        TestCase.assertEquals(ia, da)
        TestCase.assertTrue(ia is LuaInteger)
        TestCase.assertTrue(da is LuaInteger)
        TestCase.assertTrue(db is LuaDouble)
        TestCase.assertEquals(ia.toint(), 345)
        TestCase.assertEquals(da.toint(), 345)
        TestCase.assertEquals(da.todouble(), 345.0)
        TestCase.assertEquals(db.todouble(), 345.5)

        TestCase.assertTrue(sa is LuaString)
        TestCase.assertTrue(sb is LuaString)
        TestCase.assertTrue(sc is LuaString)
        TestCase.assertTrue(sd is LuaString)
        TestCase.assertEquals(3.0, sa.todouble())
        TestCase.assertEquals(3.0, sb.todouble())
        TestCase.assertEquals(-2.0, sc.todouble())
        TestCase.assertEquals(-2.0, sd.todouble())

    }


    fun testEqualsInt() {
        val ia = LuaInteger.valueOf(345)
        val ib = LuaInteger.valueOf(345)
        val ic = LuaInteger.valueOf(-345)
        val sa = LuaString.valueOf("345")
        val sb = LuaString.valueOf("345")
        val sc = LuaString.valueOf("-345")

        // objects should be different
        TestCase.assertNotSame(ia, ib)
        TestCase.assertSame(sa, sb)
        TestCase.assertNotSame(ia, ic)
        TestCase.assertNotSame(sa, sc)

        // assert equals for same type
        TestCase.assertEquals(ia, ib)
        TestCase.assertEquals(sa, sb)
        TestCase.assertFalse(ia == ic)
        TestCase.assertFalse(sa == sc)

        // check object equality for different types
        TestCase.assertNotSame(ia, sa)
        TestCase.assertNotSame(sa, ia)
    }

    fun testEqualsDouble() {
        val da = LuaDouble.valueOf(345.5)
        val db = LuaDouble.valueOf(345.5)
        val dc = LuaDouble.valueOf(-345.5)
        val sa = LuaString.valueOf("345.5")
        val sb = LuaString.valueOf("345.5")
        val sc = LuaString.valueOf("-345.5")

        // objects should be different
        TestCase.assertNotSame(da, db)
        TestCase.assertSame(sa, sb)
        TestCase.assertNotSame(da, dc)
        TestCase.assertNotSame(sa, sc)

        // assert equals for same type
        TestCase.assertEquals(da, db)
        TestCase.assertEquals(sa, sb)
        TestCase.assertFalse(da == dc)
        TestCase.assertFalse(sa == sc)

        // check object equality for different types
        TestCase.assertFalse(da == sa)
        TestCase.assertFalse(sa == da)
    }

    fun testEqInt() {
        val ia = LuaInteger.valueOf(345)
        val ib = LuaInteger.valueOf(345)
        val ic = LuaInteger.valueOf(-123)
        val sa = LuaString.valueOf("345")
        val sb = LuaString.valueOf("345")
        val sc = LuaString.valueOf("-345")

        // check arithmetic equality among same types
        TestCase.assertEquals(ia.eq(ib), LuaValue.TRUE)
        TestCase.assertEquals(sa.eq(sb), LuaValue.TRUE)
        TestCase.assertEquals(ia.eq(ic), LuaValue.FALSE)
        TestCase.assertEquals(sa.eq(sc), LuaValue.FALSE)

        // check arithmetic equality among different types
        TestCase.assertEquals(ia.eq(sa), LuaValue.FALSE)
        TestCase.assertEquals(sa.eq(ia), LuaValue.FALSE)

        // equals with mismatched types
        val t = LuaTable()
        TestCase.assertEquals(ia.eq(t), LuaValue.FALSE)
        TestCase.assertEquals(t.eq(ia), LuaValue.FALSE)
        TestCase.assertEquals(ia.eq(LuaValue.FALSE), LuaValue.FALSE)
        TestCase.assertEquals(LuaValue.FALSE.eq(ia), LuaValue.FALSE)
        TestCase.assertEquals(ia.eq(LuaValue.NIL), LuaValue.FALSE)
        TestCase.assertEquals(LuaValue.NIL.eq(ia), LuaValue.FALSE)
    }

    fun testEqDouble() {
        val da = LuaDouble.valueOf(345.5)
        val db = LuaDouble.valueOf(345.5)
        val dc = LuaDouble.valueOf(-345.5)
        val sa = LuaString.valueOf("345.5")
        val sb = LuaString.valueOf("345.5")
        val sc = LuaString.valueOf("-345.5")

        // check arithmetic equality among same types
        TestCase.assertEquals(da.eq(db), LuaValue.TRUE)
        TestCase.assertEquals(sa.eq(sb), LuaValue.TRUE)
        TestCase.assertEquals(da.eq(dc), LuaValue.FALSE)
        TestCase.assertEquals(sa.eq(sc), LuaValue.FALSE)

        // check arithmetic equality among different types
        TestCase.assertEquals(da.eq(sa), LuaValue.FALSE)
        TestCase.assertEquals(sa.eq(da), LuaValue.FALSE)

        // equals with mismatched types
        val t = LuaTable()
        TestCase.assertEquals(da.eq(t), LuaValue.FALSE)
        TestCase.assertEquals(t.eq(da), LuaValue.FALSE)
        TestCase.assertEquals(da.eq(LuaValue.FALSE), LuaValue.FALSE)
        TestCase.assertEquals(LuaValue.FALSE.eq(da), LuaValue.FALSE)
        TestCase.assertEquals(da.eq(LuaValue.NIL), LuaValue.FALSE)
        TestCase.assertEquals(LuaValue.NIL.eq(da), LuaValue.FALSE)
    }


    fun testEqualsMetatag() {
        val tru = LuaValue.TRUE
        val fal = LuaValue.FALSE
        val zer = LuaValue.ZERO
        val one = LuaValue.ONE
        val abc = LuaValue.valueOf("abcdef").substring(0, 3)
        val def = LuaValue.valueOf("abcdef").substring(3, 6)
        val pi = LuaValue.valueOf(Math.PI)
        val ee = LuaValue.valueOf(Math.E)
        val tbl = LuaTable()
        val tbl2 = LuaTable()
        val tbl3 = LuaTable()
        val uda = LuaUserdata(Any())
        val udb = LuaUserdata(uda.touserdata())
        val uda2 = LuaUserdata(Any())
        val uda3 = LuaUserdata(uda.touserdata())
        val nilb = LuaValue.valueOf(LuaValue.NIL.toboolean())
        val oneb = LuaValue.valueOf(LuaValue.ONE.toboolean())
        TestCase.assertEquals(LuaValue.FALSE, nilb)
        TestCase.assertEquals(LuaValue.TRUE, oneb)
        val smt = LuaString.s_metatable
        try {
            // always return nil0
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL))
            LuaNumber.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL))
            LuaString.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL))
            tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))
            tbl2.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))
            uda.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))
            udb.setmetatable(uda.getmetatable())
            uda2.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))
            // diff metatag function
            tbl3.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))
            uda3.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))

            // primitive types or same valu do not invoke metatag as per C implementation
            TestCase.assertEquals(tru, tru.eq(tru))
            TestCase.assertEquals(tru, one.eq(one))
            TestCase.assertEquals(tru, abc.eq(abc))
            TestCase.assertEquals(tru, tbl.eq(tbl))
            TestCase.assertEquals(tru, uda.eq(uda))
            TestCase.assertEquals(tru, uda.eq(udb))
            TestCase.assertEquals(fal, tru.eq(fal))
            TestCase.assertEquals(fal, fal.eq(tru))
            TestCase.assertEquals(fal, zer.eq(one))
            TestCase.assertEquals(fal, one.eq(zer))
            TestCase.assertEquals(fal, pi.eq(ee))
            TestCase.assertEquals(fal, ee.eq(pi))
            TestCase.assertEquals(fal, pi.eq(one))
            TestCase.assertEquals(fal, one.eq(pi))
            TestCase.assertEquals(fal, abc.eq(def))
            TestCase.assertEquals(fal, def.eq(abc))
            // different types.  not comparable
            TestCase.assertEquals(fal, fal.eq(tbl))
            TestCase.assertEquals(fal, tbl.eq(fal))
            TestCase.assertEquals(fal, tbl.eq(one))
            TestCase.assertEquals(fal, one.eq(tbl))
            TestCase.assertEquals(fal, fal.eq(one))
            TestCase.assertEquals(fal, one.eq(fal))
            TestCase.assertEquals(fal, abc.eq(one))
            TestCase.assertEquals(fal, one.eq(abc))
            TestCase.assertEquals(fal, tbl.eq(uda))
            TestCase.assertEquals(fal, uda.eq(tbl))
            // same type, same value, does not invoke metatag op
            TestCase.assertEquals(tru, tbl.eq(tbl))
            // same type, different value, same metatag op.  comparabile via metatag op
            TestCase.assertEquals(nilb, tbl.eq(tbl2))
            TestCase.assertEquals(nilb, tbl2.eq(tbl))
            TestCase.assertEquals(nilb, uda.eq(uda2))
            TestCase.assertEquals(nilb, uda2.eq(uda))
            // same type, different metatag ops.  not comparable
            TestCase.assertEquals(fal, tbl.eq(tbl3))
            TestCase.assertEquals(fal, tbl3.eq(tbl))
            TestCase.assertEquals(fal, uda.eq(uda3))
            TestCase.assertEquals(fal, uda3.eq(uda))

            // always use right argument
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE))
            LuaNumber.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE))
            LuaString.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE))
            tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))
            tbl2.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))
            uda.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))
            udb.setmetatable(uda.getmetatable())
            uda2.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_ONE)))
            // diff metatag function
            tbl3.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))
            uda3.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.EQ, RETURN_NIL)))

            // primitive types or same value do not invoke metatag as per C implementation
            TestCase.assertEquals(tru, tru.eq(tru))
            TestCase.assertEquals(tru, one.eq(one))
            TestCase.assertEquals(tru, abc.eq(abc))
            TestCase.assertEquals(tru, tbl.eq(tbl))
            TestCase.assertEquals(tru, uda.eq(uda))
            TestCase.assertEquals(tru, uda.eq(udb))
            TestCase.assertEquals(fal, tru.eq(fal))
            TestCase.assertEquals(fal, fal.eq(tru))
            TestCase.assertEquals(fal, zer.eq(one))
            TestCase.assertEquals(fal, one.eq(zer))
            TestCase.assertEquals(fal, pi.eq(ee))
            TestCase.assertEquals(fal, ee.eq(pi))
            TestCase.assertEquals(fal, pi.eq(one))
            TestCase.assertEquals(fal, one.eq(pi))
            TestCase.assertEquals(fal, abc.eq(def))
            TestCase.assertEquals(fal, def.eq(abc))
            // different types.  not comparable
            TestCase.assertEquals(fal, fal.eq(tbl))
            TestCase.assertEquals(fal, tbl.eq(fal))
            TestCase.assertEquals(fal, tbl.eq(one))
            TestCase.assertEquals(fal, one.eq(tbl))
            TestCase.assertEquals(fal, fal.eq(one))
            TestCase.assertEquals(fal, one.eq(fal))
            TestCase.assertEquals(fal, abc.eq(one))
            TestCase.assertEquals(fal, one.eq(abc))
            TestCase.assertEquals(fal, tbl.eq(uda))
            TestCase.assertEquals(fal, uda.eq(tbl))
            // same type, same value, does not invoke metatag op
            TestCase.assertEquals(tru, tbl.eq(tbl))
            // same type, different value, same metatag op.  comparabile via metatag op
            TestCase.assertEquals(oneb, tbl.eq(tbl2))
            TestCase.assertEquals(oneb, tbl2.eq(tbl))
            TestCase.assertEquals(oneb, uda.eq(uda2))
            TestCase.assertEquals(oneb, uda2.eq(uda))
            // same type, different metatag ops.  not comparable
            TestCase.assertEquals(fal, tbl.eq(tbl3))
            TestCase.assertEquals(fal, tbl3.eq(tbl))
            TestCase.assertEquals(fal, uda.eq(uda3))
            TestCase.assertEquals(fal, uda3.eq(uda))

        } finally {
            LuaBoolean.s_metatable = null
            LuaNumber.s_metatable = null
            LuaString.s_metatable = smt
        }
    }


    fun testAdd() {
        val ia = LuaValue.valueOf(111)
        val ib = LuaValue.valueOf(44)
        val da = LuaValue.valueOf(55.25)
        val db = LuaValue.valueOf(3.5)
        val sa = LuaValue.valueOf("22.125")
        val sb = LuaValue.valueOf("7.25")

        // check types
        TestCase.assertTrue(ia is LuaInteger)
        TestCase.assertTrue(ib is LuaInteger)
        TestCase.assertTrue(da is LuaDouble)
        TestCase.assertTrue(db is LuaDouble)
        TestCase.assertTrue(sa is LuaString)
        TestCase.assertTrue(sb is LuaString)

        // like kinds
        TestCase.assertEquals(155.0, ia.add(ib).todouble())
        TestCase.assertEquals(58.75, da.add(db).todouble())
        TestCase.assertEquals(29.375, sa.add(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(166.25, ia.add(da).todouble())
        TestCase.assertEquals(166.25, da.add(ia).todouble())
        TestCase.assertEquals(133.125, ia.add(sa).todouble())
        TestCase.assertEquals(133.125, sa.add(ia).todouble())
        TestCase.assertEquals(77.375, da.add(sa).todouble())
        TestCase.assertEquals(77.375, sa.add(da).todouble())
    }

    fun testSub() {
        val ia = LuaValue.valueOf(111)
        val ib = LuaValue.valueOf(44)
        val da = LuaValue.valueOf(55.25)
        val db = LuaValue.valueOf(3.5)
        val sa = LuaValue.valueOf("22.125")
        val sb = LuaValue.valueOf("7.25")

        // like kinds
        TestCase.assertEquals(67.0, ia.sub(ib).todouble())
        TestCase.assertEquals(51.75, da.sub(db).todouble())
        TestCase.assertEquals(14.875, sa.sub(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(55.75, ia.sub(da).todouble())
        TestCase.assertEquals(-55.75, da.sub(ia).todouble())
        TestCase.assertEquals(88.875, ia.sub(sa).todouble())
        TestCase.assertEquals(-88.875, sa.sub(ia).todouble())
        TestCase.assertEquals(33.125, da.sub(sa).todouble())
        TestCase.assertEquals(-33.125, sa.sub(da).todouble())
    }

    fun testMul() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")

        // like kinds
        TestCase.assertEquals(12.0, ia.mul(ib).todouble())
        TestCase.assertEquals(.125, da.mul(db).todouble())
        TestCase.assertEquals(3.0, sa.mul(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(.75, ia.mul(da).todouble())
        TestCase.assertEquals(.75, da.mul(ia).todouble())
        TestCase.assertEquals(4.5, ia.mul(sa).todouble())
        TestCase.assertEquals(4.5, sa.mul(ia).todouble())
        TestCase.assertEquals(.375, da.mul(sa).todouble())
        TestCase.assertEquals(.375, sa.mul(da).todouble())
    }

    fun testDiv() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")

        // like kinds
        TestCase.assertEquals(3.0 / 4.0, ia.div(ib).todouble())
        TestCase.assertEquals(.25 / .5, da.div(db).todouble())
        TestCase.assertEquals(1.5 / 2.0, sa.div(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(3.0 / .25, ia.div(da).todouble())
        TestCase.assertEquals(.25 / 3.0, da.div(ia).todouble())
        TestCase.assertEquals(3.0 / 1.5, ia.div(sa).todouble())
        TestCase.assertEquals(1.5 / 3.0, sa.div(ia).todouble())
        TestCase.assertEquals(.25 / 1.5, da.div(sa).todouble())
        TestCase.assertEquals(1.5 / .25, sa.div(da).todouble())
    }

    fun testPow() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(4.0)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")

        // like kinds
        TestCase.assertEquals(Math.pow(3.0, 4.0), ia.pow(ib).todouble())
        TestCase.assertEquals(Math.pow(4.0, .5), da.pow(db).todouble())
        TestCase.assertEquals(Math.pow(1.5, 2.0), sa.pow(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(Math.pow(3.0, 4.0), ia.pow(da).todouble())
        TestCase.assertEquals(Math.pow(4.0, 3.0), da.pow(ia).todouble())
        TestCase.assertEquals(Math.pow(3.0, 1.5), ia.pow(sa).todouble())
        TestCase.assertEquals(Math.pow(1.5, 3.0), sa.pow(ia).todouble())
        TestCase.assertEquals(Math.pow(4.0, 1.5), da.pow(sa).todouble())
        TestCase.assertEquals(Math.pow(1.5, 4.0), sa.pow(da).todouble())
    }

    fun testMod() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(-4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(-.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("-2.0")

        // like kinds
        TestCase.assertEquals(luaMod(3.0, -4.0), ia.mod(ib).todouble())
        TestCase.assertEquals(luaMod(.25, -.5), da.mod(db).todouble())
        TestCase.assertEquals(luaMod(1.5, -2.0), sa.mod(sb).todouble())

        // unlike kinds
        TestCase.assertEquals(luaMod(3.0, .25), ia.mod(da).todouble())
        TestCase.assertEquals(luaMod(.25, 3.0), da.mod(ia).todouble())
        TestCase.assertEquals(luaMod(3.0, 1.5), ia.mod(sa).todouble())
        TestCase.assertEquals(luaMod(1.5, 3.0), sa.mod(ia).todouble())
        TestCase.assertEquals(luaMod(.25, 1.5), da.mod(sa).todouble())
        TestCase.assertEquals(luaMod(1.5, .25), sa.mod(da).todouble())
    }

    fun testArithErrors() {
        val ia = LuaValue.valueOf(111)
        val ib = LuaValue.valueOf(44)
        val da = LuaValue.valueOf(55.25)
        val db = LuaValue.valueOf(3.5)
        val sa = LuaValue.valueOf("22.125")
        val sb = LuaValue.valueOf("7.25")

        val ops = arrayOf("add", "sub", "mul", "div", "mod", "pow")
        val vals = arrayOf(LuaValue.NIL, LuaValue.TRUE, LuaValue.tableOf())
        val numerics = arrayOf(LuaValue.valueOf(111), LuaValue.valueOf(55.25), LuaValue.valueOf("22.125"))
        for (i in ops.indices) {
            for (j in vals.indices) {
                for (k in numerics.indices) {
                    checkArithError(vals[j], numerics[k], ops[i], vals[j].typename())
                    checkArithError(numerics[k], vals[j], ops[i], vals[j].typename())
                }
            }
        }
    }

    private fun checkArithError(a: LuaValue, b: LuaValue, op: String, type: String) {
        try {
            LuaValue::class.java.getMethod(op, *arrayOf<Class<*>>(LuaValue::class.java)).invoke(a, *arrayOf<Any>(b))
        } catch (ite: InvocationTargetException) {
            val actual = ite.targetException.message!!
            if (!actual.startsWith("attempt to perform arithmetic") || actual.indexOf(type) < 0)
                TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") reported '" + actual + "'")
        } catch (e: Exception) {
            TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") threw " + e)
        }

    }

    fun testArithMetatag() {
        val tru = LuaValue.TRUE
        val fal = LuaValue.FALSE
        val tbl = LuaTable()
        val tbl2 = LuaTable()
        try {
            try {
                tru.add(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.mul(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.div(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.pow(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.mod(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            // always use left argument
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.ADD, RETURN_LHS))
            TestCase.assertEquals(tru, tru.add(fal))
            TestCase.assertEquals(tru, tru.add(tbl))
            TestCase.assertEquals(tbl, tbl.add(tru))
            try {
                tbl.add(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.SUB, RETURN_LHS))
            TestCase.assertEquals(tru, tru.sub(fal))
            TestCase.assertEquals(tru, tru.sub(tbl))
            TestCase.assertEquals(tbl, tbl.sub(tru))
            try {
                tbl.sub(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.add(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.MUL, RETURN_LHS))
            TestCase.assertEquals(tru, tru.mul(fal))
            TestCase.assertEquals(tru, tru.mul(tbl))
            TestCase.assertEquals(tbl, tbl.mul(tru))
            try {
                tbl.mul(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.DIV, RETURN_LHS))
            TestCase.assertEquals(tru, tru.div(fal))
            TestCase.assertEquals(tru, tru.div(tbl))
            TestCase.assertEquals(tbl, tbl.div(tru))
            try {
                tbl.div(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.POW, RETURN_LHS))
            TestCase.assertEquals(tru, tru.pow(fal))
            TestCase.assertEquals(tru, tru.pow(tbl))
            TestCase.assertEquals(tbl, tbl.pow(tru))
            try {
                tbl.pow(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.MOD, RETURN_LHS))
            TestCase.assertEquals(tru, tru.mod(fal))
            TestCase.assertEquals(tru, tru.mod(tbl))
            TestCase.assertEquals(tbl, tbl.mod(tru))
            try {
                tbl.mod(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }


            // always use right argument
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.ADD, RETURN_RHS))
            TestCase.assertEquals(fal, tru.add(fal))
            TestCase.assertEquals(tbl, tru.add(tbl))
            TestCase.assertEquals(tru, tbl.add(tru))
            try {
                tbl.add(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.SUB, RETURN_RHS))
            TestCase.assertEquals(fal, tru.sub(fal))
            TestCase.assertEquals(tbl, tru.sub(tbl))
            TestCase.assertEquals(tru, tbl.sub(tru))
            try {
                tbl.sub(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.add(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.MUL, RETURN_RHS))
            TestCase.assertEquals(fal, tru.mul(fal))
            TestCase.assertEquals(tbl, tru.mul(tbl))
            TestCase.assertEquals(tru, tbl.mul(tru))
            try {
                tbl.mul(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.DIV, RETURN_RHS))
            TestCase.assertEquals(fal, tru.div(fal))
            TestCase.assertEquals(tbl, tru.div(tbl))
            TestCase.assertEquals(tru, tbl.div(tru))
            try {
                tbl.div(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.POW, RETURN_RHS))
            TestCase.assertEquals(fal, tru.pow(fal))
            TestCase.assertEquals(tbl, tru.pow(tbl))
            TestCase.assertEquals(tru, tbl.pow(tru))
            try {
                tbl.pow(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.MOD, RETURN_RHS))
            TestCase.assertEquals(fal, tru.mod(fal))
            TestCase.assertEquals(tbl, tru.mod(tbl))
            TestCase.assertEquals(tru, tbl.mod(tru))
            try {
                tbl.mod(tbl2)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tru.sub(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }


        } finally {
            LuaBoolean.s_metatable = null
        }
    }

    fun testArithMetatagNumberTable() {
        val zero = LuaValue.ZERO
        val one = LuaValue.ONE
        val tbl = LuaTable()

        try {
            tbl.add(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.add(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.ADD, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.add(zero))
        TestCase.assertEquals(one, zero.add(tbl))

        try {
            tbl.sub(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.sub(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.SUB, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.sub(zero))
        TestCase.assertEquals(one, zero.sub(tbl))

        try {
            tbl.mul(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.mul(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.MUL, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.mul(zero))
        TestCase.assertEquals(one, zero.mul(tbl))

        try {
            tbl.div(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.div(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.DIV, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.div(zero))
        TestCase.assertEquals(one, zero.div(tbl))

        try {
            tbl.pow(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.pow(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.POW, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.pow(zero))
        TestCase.assertEquals(one, zero.pow(tbl))

        try {
            tbl.mod(zero)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        try {
            zero.mod(tbl)
            TestCase.fail("did not throw error")
        } catch (le: LuaError) {
        }

        tbl.setmetatable(LuaValue.tableOf(arrayOf(LuaValue.MOD, RETURN_ONE)))
        TestCase.assertEquals(one, tbl.mod(zero))
        TestCase.assertEquals(one, zero.mod(tbl))
    }

    fun testCompareStrings() {
        // these are lexical compare!
        val sa = LuaValue.valueOf("-1.5")
        val sb = LuaValue.valueOf("-2.0")
        val sc = LuaValue.valueOf("1.5")
        val sd = LuaValue.valueOf("2.0")

        TestCase.assertEquals(LuaValue.FALSE, sa.lt(sa))
        TestCase.assertEquals(LuaValue.TRUE, sa.lt(sb))
        TestCase.assertEquals(LuaValue.TRUE, sa.lt(sc))
        TestCase.assertEquals(LuaValue.TRUE, sa.lt(sd))
        TestCase.assertEquals(LuaValue.FALSE, sb.lt(sa))
        TestCase.assertEquals(LuaValue.FALSE, sb.lt(sb))
        TestCase.assertEquals(LuaValue.TRUE, sb.lt(sc))
        TestCase.assertEquals(LuaValue.TRUE, sb.lt(sd))
        TestCase.assertEquals(LuaValue.FALSE, sc.lt(sa))
        TestCase.assertEquals(LuaValue.FALSE, sc.lt(sb))
        TestCase.assertEquals(LuaValue.FALSE, sc.lt(sc))
        TestCase.assertEquals(LuaValue.TRUE, sc.lt(sd))
        TestCase.assertEquals(LuaValue.FALSE, sd.lt(sa))
        TestCase.assertEquals(LuaValue.FALSE, sd.lt(sb))
        TestCase.assertEquals(LuaValue.FALSE, sd.lt(sc))
        TestCase.assertEquals(LuaValue.FALSE, sd.lt(sd))
    }

    fun testLt() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)

        // like kinds
        TestCase.assertEquals(3.0 < 4.0, ia.lt(ib).toboolean())
        TestCase.assertEquals(.25 < .5, da.lt(db).toboolean())
        TestCase.assertEquals(3.0 < 4.0, ia.lt_b(ib))
        TestCase.assertEquals(.25 < .5, da.lt_b(db))

        // unlike kinds
        TestCase.assertEquals(3.0 < .25, ia.lt(da).toboolean())
        TestCase.assertEquals(.25 < 3.0, da.lt(ia).toboolean())
        TestCase.assertEquals(3.0 < .25, ia.lt_b(da))
        TestCase.assertEquals(.25 < 3.0, da.lt_b(ia))
    }

    fun testLtEq() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)

        // like kinds
        TestCase.assertEquals(3.0 <= 4.0, ia.lteq(ib).toboolean())
        TestCase.assertEquals(.25 <= .5, da.lteq(db).toboolean())
        TestCase.assertEquals(3.0 <= 4.0, ia.lteq_b(ib))
        TestCase.assertEquals(.25 <= .5, da.lteq_b(db))

        // unlike kinds
        TestCase.assertEquals(3.0 <= .25, ia.lteq(da).toboolean())
        TestCase.assertEquals(.25 <= 3.0, da.lteq(ia).toboolean())
        TestCase.assertEquals(3.0 <= .25, ia.lteq_b(da))
        TestCase.assertEquals(.25 <= 3.0, da.lteq_b(ia))
    }

    fun testGt() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)

        // like kinds
        TestCase.assertEquals(3.0 > 4.0, ia.gt(ib).toboolean())
        TestCase.assertEquals(.25 > .5, da.gt(db).toboolean())
        TestCase.assertEquals(3.0 > 4.0, ia.gt_b(ib))
        TestCase.assertEquals(.25 > .5, da.gt_b(db))

        // unlike kinds
        TestCase.assertEquals(3.0 > .25, ia.gt(da).toboolean())
        TestCase.assertEquals(.25 > 3.0, da.gt(ia).toboolean())
        TestCase.assertEquals(3.0 > .25, ia.gt_b(da))
        TestCase.assertEquals(.25 > 3.0, da.gt_b(ia))
    }

    fun testGtEq() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)

        // like kinds
        TestCase.assertEquals(3.0 >= 4.0, ia.gteq(ib).toboolean())
        TestCase.assertEquals(.25 >= .5, da.gteq(db).toboolean())
        TestCase.assertEquals(3.0 >= 4.0, ia.gteq_b(ib))
        TestCase.assertEquals(.25 >= .5, da.gteq_b(db))

        // unlike kinds
        TestCase.assertEquals(3.0 >= .25, ia.gteq(da).toboolean())
        TestCase.assertEquals(.25 >= 3.0, da.gteq(ia).toboolean())
        TestCase.assertEquals(3.0 >= .25, ia.gteq_b(da))
        TestCase.assertEquals(.25 >= 3.0, da.gteq_b(ia))
    }

    fun testNotEq() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")

        // like kinds
        TestCase.assertEquals(3.0 != 4.0, ia.neq(ib).toboolean())
        TestCase.assertEquals(.25 != .5, da.neq(db).toboolean())
        TestCase.assertEquals(1.5 != 2.0, sa.neq(sb).toboolean())
        TestCase.assertEquals(3.0 != 4.0, ia.neq_b(ib))
        TestCase.assertEquals(.25 != .5, da.neq_b(db))
        TestCase.assertEquals(1.5 != 2.0, sa.neq_b(sb))

        // unlike kinds
        TestCase.assertEquals(3.0 != .25, ia.neq(da).toboolean())
        TestCase.assertEquals(.25 != 3.0, da.neq(ia).toboolean())
        TestCase.assertEquals(3.0 != 1.5, ia.neq(sa).toboolean())
        TestCase.assertEquals(1.5 != 3.0, sa.neq(ia).toboolean())
        TestCase.assertEquals(.25 != 1.5, da.neq(sa).toboolean())
        TestCase.assertEquals(1.5 != .25, sa.neq(da).toboolean())
        TestCase.assertEquals(3.0 != .25, ia.neq_b(da))
        TestCase.assertEquals(.25 != 3.0, da.neq_b(ia))
        TestCase.assertEquals(3.0 != 1.5, ia.neq_b(sa))
        TestCase.assertEquals(1.5 != 3.0, sa.neq_b(ia))
        TestCase.assertEquals(.25 != 1.5, da.neq_b(sa))
        TestCase.assertEquals(1.5 != .25, sa.neq_b(da))
    }


    fun testCompareErrors() {
        val ia = LuaValue.valueOf(111)
        val ib = LuaValue.valueOf(44)
        val da = LuaValue.valueOf(55.25)
        val db = LuaValue.valueOf(3.5)
        val sa = LuaValue.valueOf("22.125")
        val sb = LuaValue.valueOf("7.25")

        val ops = arrayOf("lt", "lteq")
        val vals = arrayOf(LuaValue.NIL, LuaValue.TRUE, LuaValue.tableOf())
        val numerics = arrayOf(LuaValue.valueOf(111), LuaValue.valueOf(55.25), LuaValue.valueOf("22.125"))
        for (i in ops.indices) {
            for (j in vals.indices) {
                for (k in numerics.indices) {
                    checkCompareError(vals[j], numerics[k], ops[i], vals[j].typename())
                    checkCompareError(numerics[k], vals[j], ops[i], vals[j].typename())
                }
            }
        }
    }

    private fun checkCompareError(a: LuaValue, b: LuaValue, op: String, type: String) {
        try {
            LuaValue::class.java.getMethod(op, *arrayOf<Class<*>>(LuaValue::class.java)).invoke(a, *arrayOf<Any>(b))
        } catch (ite: InvocationTargetException) {
            val actual = ite.targetException.message!!
            if (!actual.startsWith("attempt to compare") || actual.indexOf(type) < 0)
                TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") reported '" + actual + "'")
        } catch (e: Exception) {
            TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") threw " + e)
        }

    }

    fun testCompareMetatag() {
        val tru = LuaValue.TRUE
        val fal = LuaValue.FALSE
        val tbl = LuaTable()
        val tbl2 = LuaTable()
        val tbl3 = LuaTable()
        try {
            // always use left argument
            var mt: LuaValue = LuaValue.tableOf(arrayOf(LuaValue.LT, RETURN_LHS, LuaValue.LE, RETURN_RHS))
            LuaBoolean.s_metatable = mt
            tbl.setmetatable(mt)
            tbl2.setmetatable(mt)
            TestCase.assertEquals(tru, tru.lt(fal))
            TestCase.assertEquals(fal, fal.lt(tru))
            TestCase.assertEquals(tbl, tbl.lt(tbl2))
            TestCase.assertEquals(tbl2, tbl2.lt(tbl))
            TestCase.assertEquals(tbl, tbl.lt(tbl3))
            TestCase.assertEquals(tbl3, tbl3.lt(tbl))
            TestCase.assertEquals(fal, tru.lteq(fal))
            TestCase.assertEquals(tru, fal.lteq(tru))
            TestCase.assertEquals(tbl2, tbl.lteq(tbl2))
            TestCase.assertEquals(tbl, tbl2.lteq(tbl))
            TestCase.assertEquals(tbl3, tbl.lteq(tbl3))
            TestCase.assertEquals(tbl, tbl3.lteq(tbl))

            // always use right argument
            mt = LuaValue.tableOf(arrayOf(LuaValue.LT, RETURN_RHS, LuaValue.LE, RETURN_LHS))
            LuaBoolean.s_metatable = mt
            tbl.setmetatable(mt)
            tbl2.setmetatable(mt)
            TestCase.assertEquals(fal, tru.lt(fal))
            TestCase.assertEquals(tru, fal.lt(tru))
            TestCase.assertEquals(tbl2, tbl.lt(tbl2))
            TestCase.assertEquals(tbl, tbl2.lt(tbl))
            TestCase.assertEquals(tbl3, tbl.lt(tbl3))
            TestCase.assertEquals(tbl, tbl3.lt(tbl))
            TestCase.assertEquals(tru, tru.lteq(fal))
            TestCase.assertEquals(fal, fal.lteq(tru))
            TestCase.assertEquals(tbl, tbl.lteq(tbl2))
            TestCase.assertEquals(tbl2, tbl2.lteq(tbl))
            TestCase.assertEquals(tbl, tbl.lteq(tbl3))
            TestCase.assertEquals(tbl3, tbl3.lteq(tbl))

        } finally {
            LuaBoolean.s_metatable = null
        }
    }

    fun testAnd() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")
        val ba = LuaValue.TRUE
        val bb = LuaValue.FALSE

        // like kinds
        TestCase.assertSame(ib, ia.and(ib))
        TestCase.assertSame(db, da.and(db))
        TestCase.assertSame(sb, sa.and(sb))

        // unlike kinds
        TestCase.assertSame(da, ia.and(da))
        TestCase.assertSame(ia, da.and(ia))
        TestCase.assertSame(sa, ia.and(sa))
        TestCase.assertSame(ia, sa.and(ia))
        TestCase.assertSame(sa, da.and(sa))
        TestCase.assertSame(da, sa.and(da))

        // boolean values
        TestCase.assertSame(bb, ba.and(bb))
        TestCase.assertSame(bb, bb.and(ba))
        TestCase.assertSame(ia, ba.and(ia))
        TestCase.assertSame(bb, bb.and(ia))
    }


    fun testOr() {
        val ia = LuaValue.valueOf(3)
        val ib = LuaValue.valueOf(4)
        val da = LuaValue.valueOf(.25)
        val db = LuaValue.valueOf(.5)
        val sa = LuaValue.valueOf("1.5")
        val sb = LuaValue.valueOf("2.0")
        val ba = LuaValue.TRUE
        val bb = LuaValue.FALSE

        // like kinds
        TestCase.assertSame(ia, ia.or(ib))
        TestCase.assertSame(da, da.or(db))
        TestCase.assertSame(sa, sa.or(sb))

        // unlike kinds
        TestCase.assertSame(ia, ia.or(da))
        TestCase.assertSame(da, da.or(ia))
        TestCase.assertSame(ia, ia.or(sa))
        TestCase.assertSame(sa, sa.or(ia))
        TestCase.assertSame(da, da.or(sa))
        TestCase.assertSame(sa, sa.or(da))

        // boolean values
        TestCase.assertSame(ba, ba.or(bb))
        TestCase.assertSame(ba, bb.or(ba))
        TestCase.assertSame(ba, ba.or(ia))
        TestCase.assertSame(ia, bb.or(ia))
    }

    fun testLexicalComparison() {
        val aaa = LuaValue.valueOf("aaa")
        val baa = LuaValue.valueOf("baa")
        val Aaa = LuaValue.valueOf("Aaa")
        val aba = LuaValue.valueOf("aba")
        val aaaa = LuaValue.valueOf("aaaa")
        val t = LuaValue.TRUE
        val f = LuaValue.FALSE

        // basics
        TestCase.assertEquals(t, aaa.eq(aaa))
        TestCase.assertEquals(t, aaa.lt(baa))
        TestCase.assertEquals(t, aaa.lteq(baa))
        TestCase.assertEquals(f, aaa.gt(baa))
        TestCase.assertEquals(f, aaa.gteq(baa))
        TestCase.assertEquals(f, baa.lt(aaa))
        TestCase.assertEquals(f, baa.lteq(aaa))
        TestCase.assertEquals(t, baa.gt(aaa))
        TestCase.assertEquals(t, baa.gteq(aaa))
        TestCase.assertEquals(t, aaa.lteq(aaa))
        TestCase.assertEquals(t, aaa.gteq(aaa))

        // different case
        TestCase.assertEquals(t, Aaa.eq(Aaa))
        TestCase.assertEquals(t, Aaa.lt(aaa))
        TestCase.assertEquals(t, Aaa.lteq(aaa))
        TestCase.assertEquals(f, Aaa.gt(aaa))
        TestCase.assertEquals(f, Aaa.gteq(aaa))
        TestCase.assertEquals(f, aaa.lt(Aaa))
        TestCase.assertEquals(f, aaa.lteq(Aaa))
        TestCase.assertEquals(t, aaa.gt(Aaa))
        TestCase.assertEquals(t, aaa.gteq(Aaa))
        TestCase.assertEquals(t, Aaa.lteq(Aaa))
        TestCase.assertEquals(t, Aaa.gteq(Aaa))

        // second letter differs
        TestCase.assertEquals(t, aaa.eq(aaa))
        TestCase.assertEquals(t, aaa.lt(aba))
        TestCase.assertEquals(t, aaa.lteq(aba))
        TestCase.assertEquals(f, aaa.gt(aba))
        TestCase.assertEquals(f, aaa.gteq(aba))
        TestCase.assertEquals(f, aba.lt(aaa))
        TestCase.assertEquals(f, aba.lteq(aaa))
        TestCase.assertEquals(t, aba.gt(aaa))
        TestCase.assertEquals(t, aba.gteq(aaa))
        TestCase.assertEquals(t, aaa.lteq(aaa))
        TestCase.assertEquals(t, aaa.gteq(aaa))

        // longer
        TestCase.assertEquals(t, aaa.eq(aaa))
        TestCase.assertEquals(t, aaa.lt(aaaa))
        TestCase.assertEquals(t, aaa.lteq(aaaa))
        TestCase.assertEquals(f, aaa.gt(aaaa))
        TestCase.assertEquals(f, aaa.gteq(aaaa))
        TestCase.assertEquals(f, aaaa.lt(aaa))
        TestCase.assertEquals(f, aaaa.lteq(aaa))
        TestCase.assertEquals(t, aaaa.gt(aaa))
        TestCase.assertEquals(t, aaaa.gteq(aaa))
        TestCase.assertEquals(t, aaa.lteq(aaa))
        TestCase.assertEquals(t, aaa.gteq(aaa))
    }

    fun testBuffer() {
        val abc = LuaValue.valueOf("abcdefghi").substring(0, 3)
        val def = LuaValue.valueOf("abcdefghi").substring(3, 6)
        val ghi = LuaValue.valueOf("abcdefghi").substring(6, 9)
        val n123 = LuaValue.valueOf(123)

        // basic append
        var b = Buffer()
        TestCase.assertEquals("", b.value().tojstring())
        b.append(def)
        TestCase.assertEquals("def", b.value().tojstring())
        b.append(abc)
        TestCase.assertEquals("defabc", b.value().tojstring())
        b.append(ghi)
        TestCase.assertEquals("defabcghi", b.value().tojstring())
        b.append(n123)
        TestCase.assertEquals("defabcghi123", b.value().tojstring())

        // basic prepend
        b = Buffer()
        TestCase.assertEquals("", b.value().tojstring())
        b.prepend(def.strvalue()!!)
        TestCase.assertEquals("def", b.value().tojstring())
        b.prepend(ghi.strvalue()!!)
        TestCase.assertEquals("ghidef", b.value().tojstring())
        b.prepend(abc.strvalue()!!)
        TestCase.assertEquals("abcghidef", b.value().tojstring())
        b.prepend(n123.strvalue()!!)
        TestCase.assertEquals("123abcghidef", b.value().tojstring())

        // mixed append, prepend
        b = Buffer()
        TestCase.assertEquals("", b.value().tojstring())
        b.append(def)
        TestCase.assertEquals("def", b.value().tojstring())
        b.append(abc)
        TestCase.assertEquals("defabc", b.value().tojstring())
        b.prepend(ghi.strvalue()!!)
        TestCase.assertEquals("ghidefabc", b.value().tojstring())
        b.prepend(n123.strvalue()!!)
        TestCase.assertEquals("123ghidefabc", b.value().tojstring())
        b.append(def)
        TestCase.assertEquals("123ghidefabcdef", b.value().tojstring())
        b.append(abc)
        TestCase.assertEquals("123ghidefabcdefabc", b.value().tojstring())
        b.prepend(ghi.strvalue()!!)
        TestCase.assertEquals("ghi123ghidefabcdefabc", b.value().tojstring())
        b.prepend(n123.strvalue()!!)
        TestCase.assertEquals("123ghi123ghidefabcdefabc", b.value().tojstring())

        // value
        b = Buffer(def)
        TestCase.assertEquals("def", b.value().tojstring())
        b.append(abc)
        TestCase.assertEquals("defabc", b.value().tojstring())
        b.prepend(ghi.strvalue()!!)
        TestCase.assertEquals("ghidefabc", b.value().tojstring())
        b.setvalue(def)
        TestCase.assertEquals("def", b.value().tojstring())
        b.prepend(ghi.strvalue()!!)
        TestCase.assertEquals("ghidef", b.value().tojstring())
        b.append(abc)
        TestCase.assertEquals("ghidefabc", b.value().tojstring())
    }

    fun testConcat() {
        val abc = LuaValue.valueOf("abcdefghi").substring(0, 3)
        val def = LuaValue.valueOf("abcdefghi").substring(3, 6)
        val ghi = LuaValue.valueOf("abcdefghi").substring(6, 9)
        val n123 = LuaValue.valueOf(123)

        TestCase.assertEquals("abc", abc.tojstring())
        TestCase.assertEquals("def", def.tojstring())
        TestCase.assertEquals("ghi", ghi.tojstring())
        TestCase.assertEquals("123", n123.tojstring())
        TestCase.assertEquals("abcabc", abc.concat(abc).tojstring())
        TestCase.assertEquals("defghi", def.concat(ghi).tojstring())
        TestCase.assertEquals("ghidef", ghi.concat(def).tojstring())
        TestCase.assertEquals("ghidefabcghi", ghi.concat(def).concat(abc).concat(ghi).tojstring())
        TestCase.assertEquals("123def", n123.concat(def).tojstring())
        TestCase.assertEquals("def123", def.concat(n123).tojstring())
    }

    fun testConcatBuffer() {
        val abc = LuaValue.valueOf("abcdefghi").substring(0, 3)
        val def = LuaValue.valueOf("abcdefghi").substring(3, 6)
        val ghi = LuaValue.valueOf("abcdefghi").substring(6, 9)
        val n123 = LuaValue.valueOf(123)
        var b: Buffer

        b = Buffer(def)
        TestCase.assertEquals("def", b.value().tojstring())
        b = ghi.concat(b)
        TestCase.assertEquals("ghidef", b.value().tojstring())
        b = abc.concat(b)
        TestCase.assertEquals("abcghidef", b.value().tojstring())
        b = n123.concat(b)
        TestCase.assertEquals("123abcghidef", b.value().tojstring())
        b.setvalue(n123)
        b = def.concat(b)
        TestCase.assertEquals("def123", b.value().tojstring())
        b = abc.concat(b)
        TestCase.assertEquals("abcdef123", b.value().tojstring())
    }

    fun testConcatMetatag() {
        val def = LuaValue.valueOf("abcdefghi").substring(3, 6)
        val ghi = LuaValue.valueOf("abcdefghi").substring(6, 9)
        val tru = LuaValue.TRUE
        val fal = LuaValue.FALSE
        val tbl = LuaTable()
        val uda = LuaUserdata(Any())
        try {
            // always use left argument
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.CONCAT, RETURN_LHS))
            TestCase.assertEquals(tru, tru.concat(tbl))
            TestCase.assertEquals(tbl, tbl.concat(tru))
            TestCase.assertEquals(tru, tru.concat(tbl))
            TestCase.assertEquals(tbl, tbl.concat(tru))
            TestCase.assertEquals(tru, tru.concat(tbl.buffer()).value())
            TestCase.assertEquals(tbl, tbl.concat(tru.buffer()).value())
            TestCase.assertEquals(fal, fal.concat(tbl.concat(tru.buffer())).value())
            TestCase.assertEquals(uda, uda.concat(tru.concat(tbl.buffer())).value())
            try {
                tbl.concat(def)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                def.concat(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tbl.concat(def.buffer()).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                def.concat(tbl.buffer()).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                uda.concat(def.concat(tbl.buffer())).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                ghi.concat(tbl.concat(def.buffer())).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            // always use right argument
            LuaBoolean.s_metatable = LuaValue.tableOf(arrayOf(LuaValue.CONCAT, RETURN_RHS))
            TestCase.assertEquals(tbl, tru.concat(tbl))
            TestCase.assertEquals(tru, tbl.concat(tru))
            TestCase.assertEquals(tbl, tru.concat(tbl.buffer()).value())
            TestCase.assertEquals(tru, tbl.concat(tru.buffer()).value())
            TestCase.assertEquals(tru, uda.concat(tbl.concat(tru.buffer())).value())
            TestCase.assertEquals(tbl, fal.concat(tru.concat(tbl.buffer())).value())
            try {
                tbl.concat(def)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                def.concat(tbl)
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                tbl.concat(def.buffer()).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                def.concat(tbl.buffer()).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                uda.concat(def.concat(tbl.buffer())).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

            try {
                uda.concat(tbl.concat(def.buffer())).value()
                TestCase.fail("did not throw error")
            } catch (le: LuaError) {
            }

        } finally {
            LuaBoolean.s_metatable = null
        }
    }

    fun testConcatErrors() {
        val ia = LuaValue.valueOf(111)
        val ib = LuaValue.valueOf(44)
        val da = LuaValue.valueOf(55.25)
        val db = LuaValue.valueOf(3.5)
        val sa = LuaValue.valueOf("22.125")
        val sb = LuaValue.valueOf("7.25")

        val ops = arrayOf("concat")
        val vals = arrayOf(LuaValue.NIL, LuaValue.TRUE, LuaValue.tableOf())
        val numerics = arrayOf(LuaValue.valueOf(111), LuaValue.valueOf(55.25), LuaValue.valueOf("22.125"))
        for (i in ops.indices) {
            for (j in vals.indices) {
                for (k in numerics.indices) {
                    checkConcatError(vals[j], numerics[k], ops[i], vals[j].typename())
                    checkConcatError(numerics[k], vals[j], ops[i], vals[j].typename())
                }
            }
        }
    }

    private fun checkConcatError(a: LuaValue, b: LuaValue, op: String, type: String) {
        try {
            LuaValue::class.java.getMethod(op, *arrayOf<Class<*>>(LuaValue::class.java)).invoke(a, *arrayOf<Any>(b))
        } catch (ite: InvocationTargetException) {
            val actual = ite.targetException.message!!
            if (!actual.startsWith("attempt to concatenate") || actual.indexOf(type) < 0)
                TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") reported '" + actual + "'")
        } catch (e: Exception) {
            TestCase.fail("(" + a.typename() + "," + op + "," + b.typename() + ") threw " + e)
        }

    }

    companion object {

        private val RETURN_NIL = object : TwoArgFunction() {
            override fun call(lhs: LuaValue, rhs: LuaValue): LuaValue {
                return LuaValue.NIL
            }
        }

        private val RETURN_ONE = object : TwoArgFunction() {
            override fun call(lhs: LuaValue, rhs: LuaValue): LuaValue {
                return LuaValue.ONE
            }
        }

        private fun luaMod(x: Double, y: Double): Double {
            return if (y != 0.0) x - y * Math.floor(x / y) else java.lang.Double.NaN
        }

        private val RETURN_LHS = object : TwoArgFunction() {
            override fun call(lhs: LuaValue, rhs: LuaValue): LuaValue {
                return lhs
            }
        }

        private val RETURN_RHS = object : TwoArgFunction() {
            override fun call(lhs: LuaValue, rhs: LuaValue): LuaValue {
                return rhs
            }
        }
    }

}
