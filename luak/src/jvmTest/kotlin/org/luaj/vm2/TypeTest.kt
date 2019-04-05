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

import org.luaj.vm2.lib.ZeroArgFunction
import org.luaj.vm2.lib.jse.JsePlatform

class TypeTest : TestCase() {

    private val sampleint = 77
    private val samplelong = 123400000000L
    private val sampledouble = 55.25
    private val samplestringstring = "abcdef"
    private val samplestringint = sampleint.toString()
    private val samplestringlong = samplelong.toString()
    private val samplestringdouble = sampledouble.toString()
    private val sampleobject = Any()
    private val sampledata = MyData()

    private val somenil = LuaValue.NIL
    private val sometrue = LuaValue.TRUE
    private val somefalse = LuaValue.FALSE
    private val zero = LuaValue.ZERO
    private val intint = LuaValue.valueOf(sampleint)
    private val longdouble = LuaValue.valueOf(samplelong.toDouble())
    private val doubledouble = LuaValue.valueOf(sampledouble)
    private val stringstring = LuaValue.valueOf(samplestringstring)
    private val stringint = LuaValue.valueOf(samplestringint)
    private val stringlong = LuaValue.valueOf(samplestringlong)
    private val stringdouble = LuaValue.valueOf(samplestringdouble)
    private val table = LuaValue.tableOf()
    private val somefunc = object : ZeroArgFunction() {
        override fun call(): LuaValue {
            return LuaValue.NONE
        }
    }
    private val thread = LuaThread(Globals(), somefunc)
    private val someclosure = LuaClosure(Prototype(), LuaTable())
    private val userdataobj = LuaValue.userdataOf(sampleobject)
    private val userdatacls = LuaValue.userdataOf(sampledata)

    class MyData

    // ===================== type checks =======================

    fun testIsBoolean() {
        TestCase.assertEquals(false, somenil.isboolean())
        TestCase.assertEquals(true, sometrue.isboolean())
        TestCase.assertEquals(true, somefalse.isboolean())
        TestCase.assertEquals(false, zero.isboolean())
        TestCase.assertEquals(false, intint.isboolean())
        TestCase.assertEquals(false, longdouble.isboolean())
        TestCase.assertEquals(false, doubledouble.isboolean())
        TestCase.assertEquals(false, stringstring.isboolean())
        TestCase.assertEquals(false, stringint.isboolean())
        TestCase.assertEquals(false, stringlong.isboolean())
        TestCase.assertEquals(false, stringdouble.isboolean())
        TestCase.assertEquals(false, thread.isboolean())
        TestCase.assertEquals(false, table.isboolean())
        TestCase.assertEquals(false, userdataobj.isboolean())
        TestCase.assertEquals(false, userdatacls.isboolean())
        TestCase.assertEquals(false, somefunc.isboolean())
        TestCase.assertEquals(false, someclosure.isboolean())
    }

    fun testIsClosure() {
        TestCase.assertEquals(false, somenil.isclosure())
        TestCase.assertEquals(false, sometrue.isclosure())
        TestCase.assertEquals(false, somefalse.isclosure())
        TestCase.assertEquals(false, zero.isclosure())
        TestCase.assertEquals(false, intint.isclosure())
        TestCase.assertEquals(false, longdouble.isclosure())
        TestCase.assertEquals(false, doubledouble.isclosure())
        TestCase.assertEquals(false, stringstring.isclosure())
        TestCase.assertEquals(false, stringint.isclosure())
        TestCase.assertEquals(false, stringlong.isclosure())
        TestCase.assertEquals(false, stringdouble.isclosure())
        TestCase.assertEquals(false, thread.isclosure())
        TestCase.assertEquals(false, table.isclosure())
        TestCase.assertEquals(false, userdataobj.isclosure())
        TestCase.assertEquals(false, userdatacls.isclosure())
        TestCase.assertEquals(false, somefunc.isclosure())
        TestCase.assertEquals(true, someclosure.isclosure())
    }


    fun testIsFunction() {
        TestCase.assertEquals(false, somenil.isfunction())
        TestCase.assertEquals(false, sometrue.isfunction())
        TestCase.assertEquals(false, somefalse.isfunction())
        TestCase.assertEquals(false, zero.isfunction())
        TestCase.assertEquals(false, intint.isfunction())
        TestCase.assertEquals(false, longdouble.isfunction())
        TestCase.assertEquals(false, doubledouble.isfunction())
        TestCase.assertEquals(false, stringstring.isfunction())
        TestCase.assertEquals(false, stringint.isfunction())
        TestCase.assertEquals(false, stringlong.isfunction())
        TestCase.assertEquals(false, stringdouble.isfunction())
        TestCase.assertEquals(false, thread.isfunction())
        TestCase.assertEquals(false, table.isfunction())
        TestCase.assertEquals(false, userdataobj.isfunction())
        TestCase.assertEquals(false, userdatacls.isfunction())
        TestCase.assertEquals(true, somefunc.isfunction())
        TestCase.assertEquals(true, someclosure.isfunction())
    }


    fun testIsInt() {
        TestCase.assertEquals(false, somenil.isint())
        TestCase.assertEquals(false, sometrue.isint())
        TestCase.assertEquals(false, somefalse.isint())
        TestCase.assertEquals(true, zero.isint())
        TestCase.assertEquals(true, intint.isint())
        TestCase.assertEquals(false, longdouble.isint())
        TestCase.assertEquals(false, doubledouble.isint())
        TestCase.assertEquals(false, stringstring.isint())
        TestCase.assertEquals(true, stringint.isint())
        TestCase.assertEquals(false, stringdouble.isint())
        TestCase.assertEquals(false, thread.isint())
        TestCase.assertEquals(false, table.isint())
        TestCase.assertEquals(false, userdataobj.isint())
        TestCase.assertEquals(false, userdatacls.isint())
        TestCase.assertEquals(false, somefunc.isint())
        TestCase.assertEquals(false, someclosure.isint())
    }

    fun testIsIntType() {
        TestCase.assertEquals(false, somenil.isinttype())
        TestCase.assertEquals(false, sometrue.isinttype())
        TestCase.assertEquals(false, somefalse.isinttype())
        TestCase.assertEquals(true, zero.isinttype())
        TestCase.assertEquals(true, intint.isinttype())
        TestCase.assertEquals(false, longdouble.isinttype())
        TestCase.assertEquals(false, doubledouble.isinttype())
        TestCase.assertEquals(false, stringstring.isinttype())
        TestCase.assertEquals(false, stringint.isinttype())
        TestCase.assertEquals(false, stringlong.isinttype())
        TestCase.assertEquals(false, stringdouble.isinttype())
        TestCase.assertEquals(false, thread.isinttype())
        TestCase.assertEquals(false, table.isinttype())
        TestCase.assertEquals(false, userdataobj.isinttype())
        TestCase.assertEquals(false, userdatacls.isinttype())
        TestCase.assertEquals(false, somefunc.isinttype())
        TestCase.assertEquals(false, someclosure.isinttype())
    }

    fun testIsLong() {
        TestCase.assertEquals(false, somenil.islong())
        TestCase.assertEquals(false, sometrue.islong())
        TestCase.assertEquals(false, somefalse.islong())
        TestCase.assertEquals(true, intint.isint())
        TestCase.assertEquals(true, longdouble.islong())
        TestCase.assertEquals(false, doubledouble.islong())
        TestCase.assertEquals(false, stringstring.islong())
        TestCase.assertEquals(true, stringint.islong())
        TestCase.assertEquals(true, stringlong.islong())
        TestCase.assertEquals(false, stringdouble.islong())
        TestCase.assertEquals(false, thread.islong())
        TestCase.assertEquals(false, table.islong())
        TestCase.assertEquals(false, userdataobj.islong())
        TestCase.assertEquals(false, userdatacls.islong())
        TestCase.assertEquals(false, somefunc.islong())
        TestCase.assertEquals(false, someclosure.islong())
    }

    fun testIsNil() {
        TestCase.assertEquals(true, somenil.isnil())
        TestCase.assertEquals(false, sometrue.isnil())
        TestCase.assertEquals(false, somefalse.isnil())
        TestCase.assertEquals(false, zero.isnil())
        TestCase.assertEquals(false, intint.isnil())
        TestCase.assertEquals(false, longdouble.isnil())
        TestCase.assertEquals(false, doubledouble.isnil())
        TestCase.assertEquals(false, stringstring.isnil())
        TestCase.assertEquals(false, stringint.isnil())
        TestCase.assertEquals(false, stringlong.isnil())
        TestCase.assertEquals(false, stringdouble.isnil())
        TestCase.assertEquals(false, thread.isnil())
        TestCase.assertEquals(false, table.isnil())
        TestCase.assertEquals(false, userdataobj.isnil())
        TestCase.assertEquals(false, userdatacls.isnil())
        TestCase.assertEquals(false, somefunc.isnil())
        TestCase.assertEquals(false, someclosure.isnil())
    }

    fun testIsNumber() {
        TestCase.assertEquals(false, somenil.isnumber())
        TestCase.assertEquals(false, sometrue.isnumber())
        TestCase.assertEquals(false, somefalse.isnumber())
        TestCase.assertEquals(true, zero.isnumber())
        TestCase.assertEquals(true, intint.isnumber())
        TestCase.assertEquals(true, longdouble.isnumber())
        TestCase.assertEquals(true, doubledouble.isnumber())
        TestCase.assertEquals(false, stringstring.isnumber())
        TestCase.assertEquals(true, stringint.isnumber())
        TestCase.assertEquals(true, stringlong.isnumber())
        TestCase.assertEquals(true, stringdouble.isnumber())
        TestCase.assertEquals(false, thread.isnumber())
        TestCase.assertEquals(false, table.isnumber())
        TestCase.assertEquals(false, userdataobj.isnumber())
        TestCase.assertEquals(false, userdatacls.isnumber())
        TestCase.assertEquals(false, somefunc.isnumber())
        TestCase.assertEquals(false, someclosure.isnumber())
    }

    fun testIsString() {
        TestCase.assertEquals(false, somenil.isstring())
        TestCase.assertEquals(false, sometrue.isstring())
        TestCase.assertEquals(false, somefalse.isstring())
        TestCase.assertEquals(true, zero.isstring())
        TestCase.assertEquals(true, longdouble.isstring())
        TestCase.assertEquals(true, doubledouble.isstring())
        TestCase.assertEquals(true, stringstring.isstring())
        TestCase.assertEquals(true, stringint.isstring())
        TestCase.assertEquals(true, stringlong.isstring())
        TestCase.assertEquals(true, stringdouble.isstring())
        TestCase.assertEquals(false, thread.isstring())
        TestCase.assertEquals(false, table.isstring())
        TestCase.assertEquals(false, userdataobj.isstring())
        TestCase.assertEquals(false, userdatacls.isstring())
        TestCase.assertEquals(false, somefunc.isstring())
        TestCase.assertEquals(false, someclosure.isstring())
    }

    fun testIsThread() {
        TestCase.assertEquals(false, somenil.isthread())
        TestCase.assertEquals(false, sometrue.isthread())
        TestCase.assertEquals(false, somefalse.isthread())
        TestCase.assertEquals(false, intint.isthread())
        TestCase.assertEquals(false, longdouble.isthread())
        TestCase.assertEquals(false, doubledouble.isthread())
        TestCase.assertEquals(false, stringstring.isthread())
        TestCase.assertEquals(false, stringint.isthread())
        TestCase.assertEquals(false, stringdouble.isthread())
        TestCase.assertEquals(true, thread.isthread())
        TestCase.assertEquals(false, table.isthread())
        TestCase.assertEquals(false, userdataobj.isthread())
        TestCase.assertEquals(false, userdatacls.isthread())
        TestCase.assertEquals(false, somefunc.isthread())
        TestCase.assertEquals(false, someclosure.isthread())
    }

    fun testIsTable() {
        TestCase.assertEquals(false, somenil.istable())
        TestCase.assertEquals(false, sometrue.istable())
        TestCase.assertEquals(false, somefalse.istable())
        TestCase.assertEquals(false, intint.istable())
        TestCase.assertEquals(false, longdouble.istable())
        TestCase.assertEquals(false, doubledouble.istable())
        TestCase.assertEquals(false, stringstring.istable())
        TestCase.assertEquals(false, stringint.istable())
        TestCase.assertEquals(false, stringdouble.istable())
        TestCase.assertEquals(false, thread.istable())
        TestCase.assertEquals(true, table.istable())
        TestCase.assertEquals(false, userdataobj.istable())
        TestCase.assertEquals(false, userdatacls.istable())
        TestCase.assertEquals(false, somefunc.istable())
        TestCase.assertEquals(false, someclosure.istable())
    }

    fun testIsUserdata() {
        TestCase.assertEquals(false, somenil.isuserdata())
        TestCase.assertEquals(false, sometrue.isuserdata())
        TestCase.assertEquals(false, somefalse.isuserdata())
        TestCase.assertEquals(false, intint.isuserdata())
        TestCase.assertEquals(false, longdouble.isuserdata())
        TestCase.assertEquals(false, doubledouble.isuserdata())
        TestCase.assertEquals(false, stringstring.isuserdata())
        TestCase.assertEquals(false, stringint.isuserdata())
        TestCase.assertEquals(false, stringdouble.isuserdata())
        TestCase.assertEquals(false, thread.isuserdata())
        TestCase.assertEquals(false, table.isuserdata())
        TestCase.assertEquals(true, userdataobj.isuserdata())
        TestCase.assertEquals(true, userdatacls.isuserdata())
        TestCase.assertEquals(false, somefunc.isuserdata())
        TestCase.assertEquals(false, someclosure.isuserdata())
    }

    fun testIsUserdataObject() {
        TestCase.assertEquals(false, somenil.isuserdata(Any::class.java))
        TestCase.assertEquals(false, sometrue.isuserdata(Any::class.java))
        TestCase.assertEquals(false, somefalse.isuserdata(Any::class.java))
        TestCase.assertEquals(false, longdouble.isuserdata(Any::class.java))
        TestCase.assertEquals(false, doubledouble.isuserdata(Any::class.java))
        TestCase.assertEquals(false, stringstring.isuserdata(Any::class.java))
        TestCase.assertEquals(false, stringint.isuserdata(Any::class.java))
        TestCase.assertEquals(false, stringdouble.isuserdata(Any::class.java))
        TestCase.assertEquals(false, thread.isuserdata(Any::class.java))
        TestCase.assertEquals(false, table.isuserdata(Any::class.java))
        TestCase.assertEquals(true, userdataobj.isuserdata(Any::class.java))
        TestCase.assertEquals(true, userdatacls.isuserdata(Any::class.java))
        TestCase.assertEquals(false, somefunc.isuserdata(Any::class.java))
        TestCase.assertEquals(false, someclosure.isuserdata(Any::class.java))
    }

    fun testIsUserdataMyData() {
        TestCase.assertEquals(false, somenil.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, sometrue.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, somefalse.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, longdouble.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, doubledouble.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, stringstring.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, stringint.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, stringdouble.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, thread.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, table.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, userdataobj.isuserdata(MyData::class.java))
        TestCase.assertEquals(true, userdatacls.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, somefunc.isuserdata(MyData::class.java))
        TestCase.assertEquals(false, someclosure.isuserdata(MyData::class.java))
    }


    // ===================== Coerce to Java =======================

    fun testToBoolean() {
        TestCase.assertEquals(false, somenil.toboolean())
        TestCase.assertEquals(true, sometrue.toboolean())
        TestCase.assertEquals(false, somefalse.toboolean())
        TestCase.assertEquals(true, zero.toboolean())
        TestCase.assertEquals(true, intint.toboolean())
        TestCase.assertEquals(true, longdouble.toboolean())
        TestCase.assertEquals(true, doubledouble.toboolean())
        TestCase.assertEquals(true, stringstring.toboolean())
        TestCase.assertEquals(true, stringint.toboolean())
        TestCase.assertEquals(true, stringlong.toboolean())
        TestCase.assertEquals(true, stringdouble.toboolean())
        TestCase.assertEquals(true, thread.toboolean())
        TestCase.assertEquals(true, table.toboolean())
        TestCase.assertEquals(true, userdataobj.toboolean())
        TestCase.assertEquals(true, userdatacls.toboolean())
        TestCase.assertEquals(true, somefunc.toboolean())
        TestCase.assertEquals(true, someclosure.toboolean())
    }

    fun testToByte() {
        TestCase.assertEquals(0.toByte(), somenil.tobyte())
        TestCase.assertEquals(0.toByte(), somefalse.tobyte())
        TestCase.assertEquals(0.toByte(), sometrue.tobyte())
        TestCase.assertEquals(0.toByte(), zero.tobyte())
        TestCase.assertEquals(sampleint.toByte(), intint.tobyte())
        TestCase.assertEquals(samplelong.toByte(), longdouble.tobyte())
        TestCase.assertEquals(sampledouble.toByte(), doubledouble.tobyte())
        TestCase.assertEquals(0.toByte(), stringstring.tobyte())
        TestCase.assertEquals(sampleint.toByte(), stringint.tobyte())
        TestCase.assertEquals(samplelong.toByte(), stringlong.tobyte())
        TestCase.assertEquals(sampledouble.toByte(), stringdouble.tobyte())
        TestCase.assertEquals(0.toByte(), thread.tobyte())
        TestCase.assertEquals(0.toByte(), table.tobyte())
        TestCase.assertEquals(0.toByte(), userdataobj.tobyte())
        TestCase.assertEquals(0.toByte(), userdatacls.tobyte())
        TestCase.assertEquals(0.toByte(), somefunc.tobyte())
        TestCase.assertEquals(0.toByte(), someclosure.tobyte())
    }

    fun testToChar() {
        TestCase.assertEquals(0.toChar(), somenil.tochar())
        TestCase.assertEquals(0.toChar(), somefalse.tochar())
        TestCase.assertEquals(0.toChar(), sometrue.tochar())
        TestCase.assertEquals(0.toChar(), zero.tochar())
        TestCase.assertEquals(sampleint.toChar().toInt(), intint.tochar().toInt())
        TestCase.assertEquals(samplelong.toChar().toInt(), longdouble.tochar().toInt())
        TestCase.assertEquals(sampledouble.toChar().toInt(), doubledouble.tochar().toInt())
        TestCase.assertEquals(0.toChar(), stringstring.tochar())
        TestCase.assertEquals(sampleint.toChar().toInt(), stringint.tochar().toInt())
        TestCase.assertEquals(samplelong.toChar().toInt(), stringlong.tochar().toInt())
        TestCase.assertEquals(sampledouble.toChar().toInt(), stringdouble.tochar().toInt())
        TestCase.assertEquals(0.toChar(), thread.tochar())
        TestCase.assertEquals(0.toChar(), table.tochar())
        TestCase.assertEquals(0.toChar(), userdataobj.tochar())
        TestCase.assertEquals(0.toChar(), userdatacls.tochar())
        TestCase.assertEquals(0.toChar(), somefunc.tochar())
        TestCase.assertEquals(0.toChar(), someclosure.tochar())
    }

    fun testToDouble() {
        TestCase.assertEquals(0.0, somenil.todouble())
        TestCase.assertEquals(0.0, somefalse.todouble())
        TestCase.assertEquals(0.0, sometrue.todouble())
        TestCase.assertEquals(0.0, zero.todouble())
        TestCase.assertEquals(sampleint.toDouble(), intint.todouble())
        TestCase.assertEquals(samplelong.toDouble(), longdouble.todouble())
        TestCase.assertEquals(sampledouble, doubledouble.todouble())
        TestCase.assertEquals(0.toDouble(), stringstring.todouble())
        TestCase.assertEquals(sampleint.toDouble(), stringint.todouble())
        TestCase.assertEquals(samplelong.toDouble(), stringlong.todouble())
        TestCase.assertEquals(sampledouble, stringdouble.todouble())
        TestCase.assertEquals(0.0, thread.todouble())
        TestCase.assertEquals(0.0, table.todouble())
        TestCase.assertEquals(0.0, userdataobj.todouble())
        TestCase.assertEquals(0.0, userdatacls.todouble())
        TestCase.assertEquals(0.0, somefunc.todouble())
        TestCase.assertEquals(0.0, someclosure.todouble())
    }

    fun testToFloat() {
        TestCase.assertEquals(0f, somenil.tofloat())
        TestCase.assertEquals(0f, somefalse.tofloat())
        TestCase.assertEquals(0f, sometrue.tofloat())
        TestCase.assertEquals(0f, zero.tofloat())
        TestCase.assertEquals(sampleint.toFloat(), intint.tofloat())
        TestCase.assertEquals(samplelong.toFloat(), longdouble.tofloat())
        TestCase.assertEquals(sampledouble.toFloat(), doubledouble.tofloat())
        TestCase.assertEquals(0.toFloat(), stringstring.tofloat())
        TestCase.assertEquals(sampleint.toFloat(), stringint.tofloat())
        TestCase.assertEquals(samplelong.toFloat(), stringlong.tofloat())
        TestCase.assertEquals(sampledouble.toFloat(), stringdouble.tofloat())
        TestCase.assertEquals(0f, thread.tofloat())
        TestCase.assertEquals(0f, table.tofloat())
        TestCase.assertEquals(0f, userdataobj.tofloat())
        TestCase.assertEquals(0f, userdatacls.tofloat())
        TestCase.assertEquals(0f, somefunc.tofloat())
        TestCase.assertEquals(0f, someclosure.tofloat())
    }

    fun testToInt() {
        TestCase.assertEquals(0, somenil.toint())
        TestCase.assertEquals(0, somefalse.toint())
        TestCase.assertEquals(0, sometrue.toint())
        TestCase.assertEquals(0, zero.toint())
        TestCase.assertEquals(sampleint, intint.toint())
        TestCase.assertEquals(samplelong.toInt(), longdouble.toint())
        TestCase.assertEquals(sampledouble.toInt(), doubledouble.toint())
        TestCase.assertEquals(0, stringstring.toint())
        TestCase.assertEquals(sampleint, stringint.toint())
        TestCase.assertEquals(samplelong.toInt(), stringlong.toint())
        TestCase.assertEquals(sampledouble.toInt(), stringdouble.toint())
        TestCase.assertEquals(0, thread.toint())
        TestCase.assertEquals(0, table.toint())
        TestCase.assertEquals(0, userdataobj.toint())
        TestCase.assertEquals(0, userdatacls.toint())
        TestCase.assertEquals(0, somefunc.toint())
        TestCase.assertEquals(0, someclosure.toint())
    }

    fun testToLong() {
        TestCase.assertEquals(0L, somenil.tolong())
        TestCase.assertEquals(0L, somefalse.tolong())
        TestCase.assertEquals(0L, sometrue.tolong())
        TestCase.assertEquals(0L, zero.tolong())
        TestCase.assertEquals(sampleint.toLong(), intint.tolong())
        TestCase.assertEquals(samplelong, longdouble.tolong())
        TestCase.assertEquals(sampledouble.toLong(), doubledouble.tolong())
        TestCase.assertEquals(0.toLong(), stringstring.tolong())
        TestCase.assertEquals(sampleint.toLong(), stringint.tolong())
        TestCase.assertEquals(samplelong, stringlong.tolong())
        TestCase.assertEquals(sampledouble.toLong(), stringdouble.tolong())
        TestCase.assertEquals(0L, thread.tolong())
        TestCase.assertEquals(0L, table.tolong())
        TestCase.assertEquals(0L, userdataobj.tolong())
        TestCase.assertEquals(0L, userdatacls.tolong())
        TestCase.assertEquals(0L, somefunc.tolong())
        TestCase.assertEquals(0L, someclosure.tolong())
    }

    fun testToShort() {
        TestCase.assertEquals(0.toShort(), somenil.toshort())
        TestCase.assertEquals(0.toShort(), somefalse.toshort())
        TestCase.assertEquals(0.toShort(), sometrue.toshort())
        TestCase.assertEquals(0.toShort(), zero.toshort())
        TestCase.assertEquals(sampleint.toShort(), intint.toshort())
        TestCase.assertEquals(samplelong.toShort(), longdouble.toshort())
        TestCase.assertEquals(sampledouble.toShort(), doubledouble.toshort())
        TestCase.assertEquals(0.toShort(), stringstring.toshort())
        TestCase.assertEquals(sampleint.toShort(), stringint.toshort())
        TestCase.assertEquals(samplelong.toShort(), stringlong.toshort())
        TestCase.assertEquals(sampledouble.toShort(), stringdouble.toshort())
        TestCase.assertEquals(0.toShort(), thread.toshort())
        TestCase.assertEquals(0.toShort(), table.toshort())
        TestCase.assertEquals(0.toShort(), userdataobj.toshort())
        TestCase.assertEquals(0.toShort(), userdatacls.toshort())
        TestCase.assertEquals(0.toShort(), somefunc.toshort())
        TestCase.assertEquals(0.toShort(), someclosure.toshort())
    }

    fun testToString() {
        TestCase.assertEquals("nil", somenil.tojstring())
        TestCase.assertEquals("false", somefalse.tojstring())
        TestCase.assertEquals("true", sometrue.tojstring())
        TestCase.assertEquals("0", zero.tojstring())
        TestCase.assertEquals(sampleint.toString(), intint.tojstring())
        TestCase.assertEquals(samplelong.toString(), longdouble.tojstring())
        TestCase.assertEquals(sampledouble.toString(), doubledouble.tojstring())
        TestCase.assertEquals(samplestringstring, stringstring.tojstring())
        TestCase.assertEquals(sampleint.toString(), stringint.tojstring())
        TestCase.assertEquals(samplelong.toString(), stringlong.tojstring())
        TestCase.assertEquals(sampledouble.toString(), stringdouble.tojstring())
        TestCase.assertEquals("thread: ", thread.tojstring().substring(0, 8))
        TestCase.assertEquals("table: ", table.tojstring().substring(0, 7))
        TestCase.assertEquals(sampleobject.toString(), userdataobj.tojstring())
        TestCase.assertEquals(sampledata.toString(), userdatacls.tojstring())
        TestCase.assertEquals("function: ", somefunc.tojstring().substring(0, 10))
        TestCase.assertEquals("function: ", someclosure.tojstring().substring(0, 10))
    }

    fun testToUserdata() {
        TestCase.assertEquals(null, somenil.touserdata())
        TestCase.assertEquals(null, somefalse.touserdata())
        TestCase.assertEquals(null, sometrue.touserdata())
        TestCase.assertEquals(null, zero.touserdata())
        TestCase.assertEquals(null, intint.touserdata())
        TestCase.assertEquals(null, longdouble.touserdata())
        TestCase.assertEquals(null, doubledouble.touserdata())
        TestCase.assertEquals(null, stringstring.touserdata())
        TestCase.assertEquals(null, stringint.touserdata())
        TestCase.assertEquals(null, stringlong.touserdata())
        TestCase.assertEquals(null, stringdouble.touserdata())
        TestCase.assertEquals(null, thread.touserdata())
        TestCase.assertEquals(null, table.touserdata())
        TestCase.assertEquals(sampleobject, userdataobj.touserdata())
        TestCase.assertEquals(sampledata, userdatacls.touserdata())
        TestCase.assertEquals(null, somefunc.touserdata())
        TestCase.assertEquals(null, someclosure.touserdata())
    }


    // ===================== Optional argument conversion =======================


    private fun throwsError(obj: LuaValue, method: String, argtype: Class<*>?, argument: Any) {
        try {
            obj.javaClass.getMethod(method, argtype).invoke(obj, argument)
        } catch (e: InvocationTargetException) {
            if (e.targetException !is LuaError)
                TestCase.fail("not a LuaError: " + e.targetException)
            return  // pass
        } catch (e: Exception) {
            TestCase.fail("bad exception: $e")
        }

        TestCase.fail("failed to throw LuaError as required")
    }

    fun testOptBoolean() {
        TestCase.assertEquals(true, somenil.optboolean(true))
        TestCase.assertEquals(false, somenil.optboolean(false))
        TestCase.assertEquals(true, sometrue.optboolean(false))
        TestCase.assertEquals(false, somefalse.optboolean(true))
        throwsError(zero, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(intint, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(longdouble, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(doubledouble, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(somefunc, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(someclosure, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(stringstring, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(stringint, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(stringlong, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(stringdouble, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(thread, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(table, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(userdataobj, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
        throwsError(userdatacls, "optboolean", Boolean::class.javaPrimitiveType, java.lang.Boolean.FALSE)
    }

    fun testOptClosure() {
        TestCase.assertEquals(someclosure, somenil.optclosure(someclosure))
        TestCase.assertEquals(null, somenil.optclosure(null))
        throwsError(sometrue, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(somefalse, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(zero, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(intint, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(longdouble, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(doubledouble, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(somefunc, "optclosure", LuaClosure::class.java, someclosure)
        TestCase.assertEquals(someclosure, someclosure.optclosure(someclosure))
        TestCase.assertEquals(someclosure, someclosure.optclosure(null))
        throwsError(stringstring, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(stringint, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(stringlong, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(stringdouble, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(thread, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(table, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(userdataobj, "optclosure", LuaClosure::class.java, someclosure)
        throwsError(userdatacls, "optclosure", LuaClosure::class.java, someclosure)
    }

    fun testOptDouble() {
        TestCase.assertEquals(33.0, somenil.optdouble(33.0))
        throwsError(sometrue, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(somefalse, "optdouble", Double::class.javaPrimitiveType, 33.0)
        TestCase.assertEquals(0.0, zero.optdouble(33.0))
        TestCase.assertEquals(sampleint.toDouble(), intint.optdouble(33.0))
        TestCase.assertEquals(samplelong.toDouble(), longdouble.optdouble(33.0))
        TestCase.assertEquals(sampledouble, doubledouble.optdouble(33.0))
        throwsError(somefunc, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(someclosure, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(stringstring, "optdouble", Double::class.javaPrimitiveType, 33.0)
        TestCase.assertEquals(sampleint.toDouble(), stringint.optdouble(33.0))
        TestCase.assertEquals(samplelong.toDouble(), stringlong.optdouble(33.0))
        TestCase.assertEquals(sampledouble, stringdouble.optdouble(33.0))
        throwsError(thread, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(table, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(userdataobj, "optdouble", Double::class.javaPrimitiveType, 33.0)
        throwsError(userdatacls, "optdouble", Double::class.javaPrimitiveType, 33.0)
    }

    fun testOptFunction() {
        TestCase.assertEquals(somefunc, somenil.optfunction(somefunc))
        TestCase.assertEquals(null, somenil.optfunction(null))
        throwsError(sometrue, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(somefalse, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(zero, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(intint, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(longdouble, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(doubledouble, "optfunction", LuaFunction::class.java, somefunc)
        TestCase.assertEquals(somefunc, somefunc.optfunction(null))
        TestCase.assertEquals(someclosure, someclosure.optfunction(null))
        TestCase.assertEquals(somefunc, somefunc.optfunction(somefunc))
        TestCase.assertEquals(someclosure, someclosure.optfunction(somefunc))
        throwsError(stringstring, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(stringint, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(stringlong, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(stringdouble, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(thread, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(table, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(userdataobj, "optfunction", LuaFunction::class.java, somefunc)
        throwsError(userdatacls, "optfunction", LuaFunction::class.java, somefunc)
    }

    fun testOptInt() {
        TestCase.assertEquals(33, somenil.optint(33))
        throwsError(sometrue, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(somefalse, "optint", Int::class.javaPrimitiveType, 33)
        TestCase.assertEquals(0, zero.optint(33))
        TestCase.assertEquals(sampleint, intint.optint(33))
        TestCase.assertEquals(samplelong.toInt(), longdouble.optint(33))
        TestCase.assertEquals(sampledouble.toInt(), doubledouble.optint(33))
        throwsError(somefunc, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(someclosure, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(stringstring, "optint", Int::class.javaPrimitiveType, 33)
        TestCase.assertEquals(sampleint, stringint.optint(33))
        TestCase.assertEquals(samplelong.toInt(), stringlong.optint(33))
        TestCase.assertEquals(sampledouble.toInt(), stringdouble.optint(33))
        throwsError(thread, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(table, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(userdataobj, "optint", Int::class.javaPrimitiveType, 33)
        throwsError(userdatacls, "optint", Int::class.javaPrimitiveType, 33)
    }

    fun testOptInteger() {
        TestCase.assertEquals(LuaValue.valueOf(33), somenil.optinteger(LuaValue.valueOf(33)))
        throwsError(sometrue, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(somefalse, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        TestCase.assertEquals(zero, zero.optinteger(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampleint), intint.optinteger(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toInt()), longdouble.optinteger(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampledouble.toInt()), doubledouble.optinteger(LuaValue.valueOf(33)))
        throwsError(somefunc, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(someclosure, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(stringstring, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        TestCase.assertEquals(LuaValue.valueOf(sampleint), stringint.optinteger(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toInt()), stringlong.optinteger(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampledouble.toInt()), stringdouble.optinteger(LuaValue.valueOf(33)))
        throwsError(thread, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(table, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(userdataobj, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
        throwsError(userdatacls, "optinteger", LuaInteger::class.java, LuaValue.valueOf(33))
    }

    fun testOptLong() {
        TestCase.assertEquals(33L, somenil.optlong(33))
        throwsError(sometrue, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(somefalse, "optlong", Long::class.javaPrimitiveType, 33)
        TestCase.assertEquals(0L, zero.optlong(33))
        TestCase.assertEquals(sampleint.toLong(), intint.optlong(33))
        TestCase.assertEquals(samplelong, longdouble.optlong(33))
        TestCase.assertEquals(sampledouble.toLong(), doubledouble.optlong(33))
        throwsError(somefunc, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(someclosure, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(stringstring, "optlong", Long::class.javaPrimitiveType, 33)
        TestCase.assertEquals(sampleint.toLong(), stringint.optlong(33))
        TestCase.assertEquals(samplelong, stringlong.optlong(33))
        TestCase.assertEquals(sampledouble.toLong(), stringdouble.optlong(33))
        throwsError(thread, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(table, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(userdataobj, "optlong", Long::class.javaPrimitiveType, 33)
        throwsError(userdatacls, "optlong", Long::class.javaPrimitiveType, 33)
    }

    fun testOptNumber() {
        TestCase.assertEquals(LuaValue.valueOf(33), somenil.optnumber(LuaValue.valueOf(33)))
        throwsError(sometrue, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(somefalse, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        TestCase.assertEquals(zero, zero.optnumber(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampleint), intint.optnumber(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toDouble()), longdouble.optnumber(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampledouble), doubledouble.optnumber(LuaValue.valueOf(33)))
        throwsError(somefunc, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(someclosure, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(stringstring, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        TestCase.assertEquals(LuaValue.valueOf(sampleint), stringint.optnumber(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toDouble()), stringlong.optnumber(LuaValue.valueOf(33)))
        TestCase.assertEquals(LuaValue.valueOf(sampledouble), stringdouble.optnumber(LuaValue.valueOf(33)))
        throwsError(thread, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(table, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(userdataobj, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
        throwsError(userdatacls, "optnumber", LuaNumber::class.java, LuaValue.valueOf(33))
    }

    fun testOptTable() {
        TestCase.assertEquals(table, somenil.opttable(table))
        TestCase.assertEquals(null, somenil.opttable(null))
        throwsError(sometrue, "opttable", LuaTable::class.java, table)
        throwsError(somefalse, "opttable", LuaTable::class.java, table)
        throwsError(zero, "opttable", LuaTable::class.java, table)
        throwsError(intint, "opttable", LuaTable::class.java, table)
        throwsError(longdouble, "opttable", LuaTable::class.java, table)
        throwsError(doubledouble, "opttable", LuaTable::class.java, table)
        throwsError(somefunc, "opttable", LuaTable::class.java, table)
        throwsError(someclosure, "opttable", LuaTable::class.java, table)
        throwsError(stringstring, "opttable", LuaTable::class.java, table)
        throwsError(stringint, "opttable", LuaTable::class.java, table)
        throwsError(stringlong, "opttable", LuaTable::class.java, table)
        throwsError(stringdouble, "opttable", LuaTable::class.java, table)
        throwsError(thread, "opttable", LuaTable::class.java, table)
        TestCase.assertEquals(table, table.opttable(table))
        TestCase.assertEquals(table, table.opttable(null))
        throwsError(userdataobj, "opttable", LuaTable::class.java, table)
        throwsError(userdatacls, "opttable", LuaTable::class.java, table)
    }

    fun testOptThread() {
        TestCase.assertEquals(thread, somenil.optthread(thread))
        TestCase.assertEquals(null, somenil.optthread(null))
        throwsError(sometrue, "optthread", LuaThread::class.java, thread)
        throwsError(somefalse, "optthread", LuaThread::class.java, thread)
        throwsError(zero, "optthread", LuaThread::class.java, thread)
        throwsError(intint, "optthread", LuaThread::class.java, thread)
        throwsError(longdouble, "optthread", LuaThread::class.java, thread)
        throwsError(doubledouble, "optthread", LuaThread::class.java, thread)
        throwsError(somefunc, "optthread", LuaThread::class.java, thread)
        throwsError(someclosure, "optthread", LuaThread::class.java, thread)
        throwsError(stringstring, "optthread", LuaThread::class.java, thread)
        throwsError(stringint, "optthread", LuaThread::class.java, thread)
        throwsError(stringlong, "optthread", LuaThread::class.java, thread)
        throwsError(stringdouble, "optthread", LuaThread::class.java, thread)
        throwsError(table, "optthread", LuaThread::class.java, thread)
        TestCase.assertEquals(thread, thread.optthread(thread))
        TestCase.assertEquals(thread, thread.optthread(null))
        throwsError(userdataobj, "optthread", LuaThread::class.java, thread)
        throwsError(userdatacls, "optthread", LuaThread::class.java, thread)
    }

    fun testOptJavaString() {
        TestCase.assertEquals("xyz", somenil.optjstring("xyz"))
        TestCase.assertEquals(null, somenil.optjstring(null))
        throwsError(sometrue, "optjstring", String::class.java, "xyz")
        throwsError(somefalse, "optjstring", String::class.java, "xyz")
        TestCase.assertEquals(zero.toString(), zero.optjstring("xyz"))
        TestCase.assertEquals(intint.toString(), intint.optjstring("xyz"))
        TestCase.assertEquals(longdouble.toString(), longdouble.optjstring("xyz"))
        TestCase.assertEquals(doubledouble.toString(), doubledouble.optjstring("xyz"))
        throwsError(somefunc, "optjstring", String::class.java, "xyz")
        throwsError(someclosure, "optjstring", String::class.java, "xyz")
        TestCase.assertEquals(samplestringstring, stringstring.optjstring("xyz"))
        TestCase.assertEquals(samplestringint, stringint.optjstring("xyz"))
        TestCase.assertEquals(samplestringlong, stringlong.optjstring("xyz"))
        TestCase.assertEquals(samplestringdouble, stringdouble.optjstring("xyz"))
        throwsError(thread, "optjstring", String::class.java, "xyz")
        throwsError(table, "optjstring", String::class.java, "xyz")
        throwsError(userdataobj, "optjstring", String::class.java, "xyz")
        throwsError(userdatacls, "optjstring", String::class.java, "xyz")
    }

    fun testOptLuaString() {
        TestCase.assertEquals(LuaValue.valueOf("xyz"), somenil.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(null, somenil.optstring(null))
        throwsError(sometrue, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        throwsError(somefalse, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        TestCase.assertEquals(LuaValue.valueOf("0"), zero.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringint, intint.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringlong, longdouble.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringdouble, doubledouble.optstring(LuaValue.valueOf("xyz")))
        throwsError(somefunc, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        throwsError(someclosure, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        TestCase.assertEquals(stringstring, stringstring.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringint, stringint.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringlong, stringlong.optstring(LuaValue.valueOf("xyz")))
        TestCase.assertEquals(stringdouble, stringdouble.optstring(LuaValue.valueOf("xyz")))
        throwsError(thread, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        throwsError(table, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        throwsError(userdataobj, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
        throwsError(userdatacls, "optstring", LuaString::class.java, LuaValue.valueOf("xyz"))
    }

    fun testOptUserdata() {
        TestCase.assertEquals(sampleobject, somenil.optuserdata(sampleobject))
        TestCase.assertEquals(sampledata, somenil.optuserdata(sampledata))
        TestCase.assertEquals(null, somenil.optuserdata(null))
        throwsError(sometrue, "optuserdata", Any::class.java, sampledata)
        throwsError(somefalse, "optuserdata", Any::class.java, sampledata)
        throwsError(zero, "optuserdata", Any::class.java, sampledata)
        throwsError(intint, "optuserdata", Any::class.java, sampledata)
        throwsError(longdouble, "optuserdata", Any::class.java, sampledata)
        throwsError(doubledouble, "optuserdata", Any::class.java, sampledata)
        throwsError(somefunc, "optuserdata", Any::class.java, sampledata)
        throwsError(someclosure, "optuserdata", Any::class.java, sampledata)
        throwsError(stringstring, "optuserdata", Any::class.java, sampledata)
        throwsError(stringint, "optuserdata", Any::class.java, sampledata)
        throwsError(stringlong, "optuserdata", Any::class.java, sampledata)
        throwsError(stringdouble, "optuserdata", Any::class.java, sampledata)
        throwsError(table, "optuserdata", Any::class.java, sampledata)
        TestCase.assertEquals(sampleobject, userdataobj.optuserdata(sampledata))
        TestCase.assertEquals(sampleobject, userdataobj.optuserdata(null))
        TestCase.assertEquals(sampledata, userdatacls.optuserdata(sampleobject))
        TestCase.assertEquals(sampledata, userdatacls.optuserdata(null))
    }

    private fun throwsErrorOptUserdataClass(obj: LuaValue, arg1: Class<*>, arg2: Any) {
        try {
            obj.javaClass.getMethod("optuserdata", Class::class.java, Any::class.java).invoke(obj, arg1, arg2)
        } catch (e: InvocationTargetException) {
            if (e.targetException !is LuaError)
                TestCase.fail("not a LuaError: " + e.targetException)
            return  // pass
        } catch (e: Exception) {
            TestCase.fail("bad exception: $e")
        }

        TestCase.fail("failed to throw LuaError as required")
    }

    fun testOptUserdataClass() {
        TestCase.assertEquals(sampledata, somenil.optuserdata(MyData::class.java, sampledata))
        TestCase.assertEquals(sampleobject, somenil.optuserdata(Any::class.java, sampleobject))
        TestCase.assertEquals(null, somenil.optuserdata(null))
        throwsErrorOptUserdataClass(sometrue, Any::class.java, sampledata)
        throwsErrorOptUserdataClass(zero, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(intint, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(longdouble, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(somefunc, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(someclosure, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(stringstring, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(stringint, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(stringlong, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(stringlong, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(stringdouble, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(table, MyData::class.java, sampledata)
        throwsErrorOptUserdataClass(thread, MyData::class.java, sampledata)
        TestCase.assertEquals(sampleobject, userdataobj.optuserdata(Any::class.java, sampleobject))
        TestCase.assertEquals(sampleobject, userdataobj.optuserdata(null))
        TestCase.assertEquals(sampledata, userdatacls.optuserdata(MyData::class.java, sampledata))
        TestCase.assertEquals(sampledata, userdatacls.optuserdata(Any::class.java, sampleobject))
        TestCase.assertEquals(sampledata, userdatacls.optuserdata(null))
        // should fail due to wrong class
        try {
            val o = userdataobj.optuserdata(MyData::class.java, sampledata)
            TestCase.fail("did not throw bad type error")
            TestCase.assertTrue(o is MyData)
        } catch (le: LuaError) {
            TestCase.assertEquals("org.luaj.vm2.TypeTest\$MyData expected, got userdata", le.message)
        }

    }

    fun testOptValue() {
        TestCase.assertEquals(zero, somenil.optvalue(zero))
        TestCase.assertEquals(stringstring, somenil.optvalue(stringstring))
        TestCase.assertEquals(sometrue, sometrue.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(somefalse, somefalse.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(zero, zero.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(intint, intint.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(longdouble, longdouble.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(somefunc, somefunc.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(someclosure, someclosure.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(stringstring, stringstring.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(stringint, stringint.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(stringlong, stringlong.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(stringdouble, stringdouble.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(thread, thread.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(table, table.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(userdataobj, userdataobj.optvalue(LuaValue.TRUE))
        TestCase.assertEquals(userdatacls, userdatacls.optvalue(LuaValue.TRUE))
    }


    // ===================== Required argument conversion =======================


    private fun throwsErrorReq(obj: LuaValue, method: String) {
        try {
            obj.javaClass.getMethod(method).invoke(obj)
        } catch (e: InvocationTargetException) {
            if (e.targetException !is LuaError)
                TestCase.fail("not a LuaError: " + e.targetException)
            return  // pass
        } catch (e: Exception) {
            TestCase.fail("bad exception: $e")
        }

        TestCase.fail("failed to throw LuaError as required")
    }

    fun testCheckBoolean() {
        throwsErrorReq(somenil, "checkboolean")
        TestCase.assertEquals(true, sometrue.checkboolean())
        TestCase.assertEquals(false, somefalse.checkboolean())
        throwsErrorReq(zero, "checkboolean")
        throwsErrorReq(intint, "checkboolean")
        throwsErrorReq(longdouble, "checkboolean")
        throwsErrorReq(doubledouble, "checkboolean")
        throwsErrorReq(somefunc, "checkboolean")
        throwsErrorReq(someclosure, "checkboolean")
        throwsErrorReq(stringstring, "checkboolean")
        throwsErrorReq(stringint, "checkboolean")
        throwsErrorReq(stringlong, "checkboolean")
        throwsErrorReq(stringdouble, "checkboolean")
        throwsErrorReq(thread, "checkboolean")
        throwsErrorReq(table, "checkboolean")
        throwsErrorReq(userdataobj, "checkboolean")
        throwsErrorReq(userdatacls, "checkboolean")
    }

    fun testCheckClosure() {
        throwsErrorReq(somenil, "checkclosure")
        throwsErrorReq(sometrue, "checkclosure")
        throwsErrorReq(somefalse, "checkclosure")
        throwsErrorReq(zero, "checkclosure")
        throwsErrorReq(intint, "checkclosure")
        throwsErrorReq(longdouble, "checkclosure")
        throwsErrorReq(doubledouble, "checkclosure")
        throwsErrorReq(somefunc, "checkclosure")
        TestCase.assertEquals(someclosure, someclosure.checkclosure())
        TestCase.assertEquals(someclosure, someclosure.checkclosure())
        throwsErrorReq(stringstring, "checkclosure")
        throwsErrorReq(stringint, "checkclosure")
        throwsErrorReq(stringlong, "checkclosure")
        throwsErrorReq(stringdouble, "checkclosure")
        throwsErrorReq(thread, "checkclosure")
        throwsErrorReq(table, "checkclosure")
        throwsErrorReq(userdataobj, "checkclosure")
        throwsErrorReq(userdatacls, "checkclosure")
    }

    fun testCheckDouble() {
        throwsErrorReq(somenil, "checkdouble")
        throwsErrorReq(sometrue, "checkdouble")
        throwsErrorReq(somefalse, "checkdouble")
        TestCase.assertEquals(0.0, zero.checkdouble())
        TestCase.assertEquals(sampleint.toDouble(), intint.checkdouble())
        TestCase.assertEquals(samplelong.toDouble(), longdouble.checkdouble())
        TestCase.assertEquals(sampledouble, doubledouble.checkdouble())
        throwsErrorReq(somefunc, "checkdouble")
        throwsErrorReq(someclosure, "checkdouble")
        throwsErrorReq(stringstring, "checkdouble")
        TestCase.assertEquals(sampleint.toDouble(), stringint.checkdouble())
        TestCase.assertEquals(samplelong.toDouble(), stringlong.checkdouble())
        TestCase.assertEquals(sampledouble, stringdouble.checkdouble())
        throwsErrorReq(thread, "checkdouble")
        throwsErrorReq(table, "checkdouble")
        throwsErrorReq(userdataobj, "checkdouble")
        throwsErrorReq(userdatacls, "checkdouble")
    }

    fun testCheckFunction() {
        throwsErrorReq(somenil, "checkfunction")
        throwsErrorReq(sometrue, "checkfunction")
        throwsErrorReq(somefalse, "checkfunction")
        throwsErrorReq(zero, "checkfunction")
        throwsErrorReq(intint, "checkfunction")
        throwsErrorReq(longdouble, "checkfunction")
        throwsErrorReq(doubledouble, "checkfunction")
        TestCase.assertEquals(somefunc, somefunc.checkfunction())
        TestCase.assertEquals(someclosure, someclosure.checkfunction())
        TestCase.assertEquals(somefunc, somefunc.checkfunction())
        TestCase.assertEquals(someclosure, someclosure.checkfunction())
        throwsErrorReq(stringstring, "checkfunction")
        throwsErrorReq(stringint, "checkfunction")
        throwsErrorReq(stringlong, "checkfunction")
        throwsErrorReq(stringdouble, "checkfunction")
        throwsErrorReq(thread, "checkfunction")
        throwsErrorReq(table, "checkfunction")
        throwsErrorReq(userdataobj, "checkfunction")
        throwsErrorReq(userdatacls, "checkfunction")
    }

    fun testCheckInt() {
        throwsErrorReq(somenil, "checkint")
        throwsErrorReq(sometrue, "checkint")
        throwsErrorReq(somefalse, "checkint")
        TestCase.assertEquals(0, zero.checkint())
        TestCase.assertEquals(sampleint, intint.checkint())
        TestCase.assertEquals(samplelong.toInt(), longdouble.checkint())
        TestCase.assertEquals(sampledouble.toInt(), doubledouble.checkint())
        throwsErrorReq(somefunc, "checkint")
        throwsErrorReq(someclosure, "checkint")
        throwsErrorReq(stringstring, "checkint")
        TestCase.assertEquals(sampleint, stringint.checkint())
        TestCase.assertEquals(samplelong.toInt(), stringlong.checkint())
        TestCase.assertEquals(sampledouble.toInt(), stringdouble.checkint())
        throwsErrorReq(thread, "checkint")
        throwsErrorReq(table, "checkint")
        throwsErrorReq(userdataobj, "checkint")
        throwsErrorReq(userdatacls, "checkint")
    }

    fun testCheckInteger() {
        throwsErrorReq(somenil, "checkinteger")
        throwsErrorReq(sometrue, "checkinteger")
        throwsErrorReq(somefalse, "checkinteger")
        TestCase.assertEquals(zero, zero.checkinteger())
        TestCase.assertEquals(LuaValue.valueOf(sampleint), intint.checkinteger())
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toInt()), longdouble.checkinteger())
        TestCase.assertEquals(LuaValue.valueOf(sampledouble.toInt()), doubledouble.checkinteger())
        throwsErrorReq(somefunc, "checkinteger")
        throwsErrorReq(someclosure, "checkinteger")
        throwsErrorReq(stringstring, "checkinteger")
        TestCase.assertEquals(LuaValue.valueOf(sampleint), stringint.checkinteger())
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toInt()), stringlong.checkinteger())
        TestCase.assertEquals(LuaValue.valueOf(sampledouble.toInt()), stringdouble.checkinteger())
        throwsErrorReq(thread, "checkinteger")
        throwsErrorReq(table, "checkinteger")
        throwsErrorReq(userdataobj, "checkinteger")
        throwsErrorReq(userdatacls, "checkinteger")
    }

    fun testCheckLong() {
        throwsErrorReq(somenil, "checklong")
        throwsErrorReq(sometrue, "checklong")
        throwsErrorReq(somefalse, "checklong")
        TestCase.assertEquals(0L, zero.checklong())
        TestCase.assertEquals(sampleint.toLong(), intint.checklong())
        TestCase.assertEquals(samplelong, longdouble.checklong())
        TestCase.assertEquals(sampledouble.toLong(), doubledouble.checklong())
        throwsErrorReq(somefunc, "checklong")
        throwsErrorReq(someclosure, "checklong")
        throwsErrorReq(stringstring, "checklong")
        TestCase.assertEquals(sampleint.toLong(), stringint.checklong())
        TestCase.assertEquals(samplelong, stringlong.checklong())
        TestCase.assertEquals(sampledouble.toLong(), stringdouble.checklong())
        throwsErrorReq(thread, "checklong")
        throwsErrorReq(table, "checklong")
        throwsErrorReq(userdataobj, "checklong")
        throwsErrorReq(userdatacls, "checklong")
    }

    fun testCheckNumber() {
        throwsErrorReq(somenil, "checknumber")
        throwsErrorReq(sometrue, "checknumber")
        throwsErrorReq(somefalse, "checknumber")
        TestCase.assertEquals(zero, zero.checknumber())
        TestCase.assertEquals(LuaValue.valueOf(sampleint), intint.checknumber())
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toDouble()), longdouble.checknumber())
        TestCase.assertEquals(LuaValue.valueOf(sampledouble), doubledouble.checknumber())
        throwsErrorReq(somefunc, "checknumber")
        throwsErrorReq(someclosure, "checknumber")
        throwsErrorReq(stringstring, "checknumber")
        TestCase.assertEquals(LuaValue.valueOf(sampleint), stringint.checknumber())
        TestCase.assertEquals(LuaValue.valueOf(samplelong.toDouble()), stringlong.checknumber())
        TestCase.assertEquals(LuaValue.valueOf(sampledouble), stringdouble.checknumber())
        throwsErrorReq(thread, "checknumber")
        throwsErrorReq(table, "checknumber")
        throwsErrorReq(userdataobj, "checknumber")
        throwsErrorReq(userdatacls, "checknumber")
    }

    fun testCheckTable() {
        throwsErrorReq(somenil, "checktable")
        throwsErrorReq(sometrue, "checktable")
        throwsErrorReq(somefalse, "checktable")
        throwsErrorReq(zero, "checktable")
        throwsErrorReq(intint, "checktable")
        throwsErrorReq(longdouble, "checktable")
        throwsErrorReq(doubledouble, "checktable")
        throwsErrorReq(somefunc, "checktable")
        throwsErrorReq(someclosure, "checktable")
        throwsErrorReq(stringstring, "checktable")
        throwsErrorReq(stringint, "checktable")
        throwsErrorReq(stringlong, "checktable")
        throwsErrorReq(stringdouble, "checktable")
        throwsErrorReq(thread, "checktable")
        TestCase.assertEquals(table, table.checktable())
        TestCase.assertEquals(table, table.checktable())
        throwsErrorReq(userdataobj, "checktable")
        throwsErrorReq(userdatacls, "checktable")
    }

    fun testCheckThread() {
        throwsErrorReq(somenil, "checkthread")
        throwsErrorReq(sometrue, "checkthread")
        throwsErrorReq(somefalse, "checkthread")
        throwsErrorReq(zero, "checkthread")
        throwsErrorReq(intint, "checkthread")
        throwsErrorReq(longdouble, "checkthread")
        throwsErrorReq(doubledouble, "checkthread")
        throwsErrorReq(somefunc, "checkthread")
        throwsErrorReq(someclosure, "checkthread")
        throwsErrorReq(stringstring, "checkthread")
        throwsErrorReq(stringint, "checkthread")
        throwsErrorReq(stringlong, "checkthread")
        throwsErrorReq(stringdouble, "checkthread")
        throwsErrorReq(table, "checkthread")
        TestCase.assertEquals(thread, thread.checkthread())
        TestCase.assertEquals(thread, thread.checkthread())
        throwsErrorReq(userdataobj, "checkthread")
        throwsErrorReq(userdatacls, "checkthread")
    }

    fun testCheckJavaString() {
        throwsErrorReq(somenil, "checkjstring")
        throwsErrorReq(sometrue, "checkjstring")
        throwsErrorReq(somefalse, "checkjstring")
        TestCase.assertEquals(zero.toString(), zero.checkjstring())
        TestCase.assertEquals(intint.toString(), intint.checkjstring())
        TestCase.assertEquals(longdouble.toString(), longdouble.checkjstring())
        TestCase.assertEquals(doubledouble.toString(), doubledouble.checkjstring())
        throwsErrorReq(somefunc, "checkjstring")
        throwsErrorReq(someclosure, "checkjstring")
        TestCase.assertEquals(samplestringstring, stringstring.checkjstring())
        TestCase.assertEquals(samplestringint, stringint.checkjstring())
        TestCase.assertEquals(samplestringlong, stringlong.checkjstring())
        TestCase.assertEquals(samplestringdouble, stringdouble.checkjstring())
        throwsErrorReq(thread, "checkjstring")
        throwsErrorReq(table, "checkjstring")
        throwsErrorReq(userdataobj, "checkjstring")
        throwsErrorReq(userdatacls, "checkjstring")
    }

    fun testCheckLuaString() {
        throwsErrorReq(somenil, "checkstring")
        throwsErrorReq(sometrue, "checkstring")
        throwsErrorReq(somefalse, "checkstring")
        TestCase.assertEquals(LuaValue.valueOf("0"), zero.checkstring())
        TestCase.assertEquals(stringint, intint.checkstring())
        TestCase.assertEquals(stringlong, longdouble.checkstring())
        TestCase.assertEquals(stringdouble, doubledouble.checkstring())
        throwsErrorReq(somefunc, "checkstring")
        throwsErrorReq(someclosure, "checkstring")
        TestCase.assertEquals(stringstring, stringstring.checkstring())
        TestCase.assertEquals(stringint, stringint.checkstring())
        TestCase.assertEquals(stringlong, stringlong.checkstring())
        TestCase.assertEquals(stringdouble, stringdouble.checkstring())
        throwsErrorReq(thread, "checkstring")
        throwsErrorReq(table, "checkstring")
        throwsErrorReq(userdataobj, "checkstring")
        throwsErrorReq(userdatacls, "checkstring")
    }

    fun testCheckUserdata() {
        throwsErrorReq(somenil, "checkuserdata")
        throwsErrorReq(sometrue, "checkuserdata")
        throwsErrorReq(somefalse, "checkuserdata")
        throwsErrorReq(zero, "checkuserdata")
        throwsErrorReq(intint, "checkuserdata")
        throwsErrorReq(longdouble, "checkuserdata")
        throwsErrorReq(doubledouble, "checkuserdata")
        throwsErrorReq(somefunc, "checkuserdata")
        throwsErrorReq(someclosure, "checkuserdata")
        throwsErrorReq(stringstring, "checkuserdata")
        throwsErrorReq(stringint, "checkuserdata")
        throwsErrorReq(stringlong, "checkuserdata")
        throwsErrorReq(stringdouble, "checkuserdata")
        throwsErrorReq(table, "checkuserdata")
        TestCase.assertEquals(sampleobject, userdataobj.checkuserdata())
        TestCase.assertEquals(sampleobject, userdataobj.checkuserdata())
        TestCase.assertEquals(sampledata, userdatacls.checkuserdata())
        TestCase.assertEquals(sampledata, userdatacls.checkuserdata())
    }

    private fun throwsErrorReqCheckUserdataClass(obj: LuaValue, arg: Class<*>) {
        try {
            obj.javaClass.getMethod("checkuserdata", Class::class.java).invoke(obj, arg)
        } catch (e: InvocationTargetException) {
            if (e.targetException !is LuaError)
                TestCase.fail("not a LuaError: " + e.targetException)
            return  // pass
        } catch (e: Exception) {
            TestCase.fail("bad exception: $e")
        }

        TestCase.fail("failed to throw LuaError as required")
    }

    fun testCheckUserdataClass() {
        throwsErrorReqCheckUserdataClass(somenil, Any::class.java)
        throwsErrorReqCheckUserdataClass(somenil, MyData::class.java)
        throwsErrorReqCheckUserdataClass(sometrue, Any::class.java)
        throwsErrorReqCheckUserdataClass(zero, MyData::class.java)
        throwsErrorReqCheckUserdataClass(intint, MyData::class.java)
        throwsErrorReqCheckUserdataClass(longdouble, MyData::class.java)
        throwsErrorReqCheckUserdataClass(somefunc, MyData::class.java)
        throwsErrorReqCheckUserdataClass(someclosure, MyData::class.java)
        throwsErrorReqCheckUserdataClass(stringstring, MyData::class.java)
        throwsErrorReqCheckUserdataClass(stringint, MyData::class.java)
        throwsErrorReqCheckUserdataClass(stringlong, MyData::class.java)
        throwsErrorReqCheckUserdataClass(stringlong, MyData::class.java)
        throwsErrorReqCheckUserdataClass(stringdouble, MyData::class.java)
        throwsErrorReqCheckUserdataClass(table, MyData::class.java)
        throwsErrorReqCheckUserdataClass(thread, MyData::class.java)
        TestCase.assertEquals(sampleobject, userdataobj.checkuserdata(Any::class.java))
        TestCase.assertEquals(sampleobject, userdataobj.checkuserdata())
        TestCase.assertEquals(sampledata, userdatacls.checkuserdata(MyData::class.java))
        TestCase.assertEquals(sampledata, userdatacls.checkuserdata(Any::class.java))
        TestCase.assertEquals(sampledata, userdatacls.checkuserdata())
        // should fail due to wrong class
        try {
            val o = userdataobj.checkuserdata(MyData::class.java)
            TestCase.fail("did not throw bad type error")
            TestCase.assertTrue(o is MyData)
        } catch (le: LuaError) {
            TestCase.assertEquals("org.luaj.vm2.TypeTest\$MyData expected, got userdata", le.message)
        }

    }

    fun testCheckValue() {
        throwsErrorReq(somenil, "checknotnil")
        TestCase.assertEquals(sometrue, sometrue.checknotnil())
        TestCase.assertEquals(somefalse, somefalse.checknotnil())
        TestCase.assertEquals(zero, zero.checknotnil())
        TestCase.assertEquals(intint, intint.checknotnil())
        TestCase.assertEquals(longdouble, longdouble.checknotnil())
        TestCase.assertEquals(somefunc, somefunc.checknotnil())
        TestCase.assertEquals(someclosure, someclosure.checknotnil())
        TestCase.assertEquals(stringstring, stringstring.checknotnil())
        TestCase.assertEquals(stringint, stringint.checknotnil())
        TestCase.assertEquals(stringlong, stringlong.checknotnil())
        TestCase.assertEquals(stringdouble, stringdouble.checknotnil())
        TestCase.assertEquals(thread, thread.checknotnil())
        TestCase.assertEquals(table, table.checknotnil())
        TestCase.assertEquals(userdataobj, userdataobj.checknotnil())
        TestCase.assertEquals(userdatacls, userdatacls.checknotnil())
    }

    companion object {
        init {
            JsePlatform.debugGlobals()
        }
    }

}
