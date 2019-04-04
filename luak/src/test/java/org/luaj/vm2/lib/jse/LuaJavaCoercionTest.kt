package org.luaj.vm2.lib.jse

import junit.framework.TestCase

import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaInteger
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.MathLib

class LuaJavaCoercionTest : TestCase() {

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        globals = JsePlatform.standardGlobals()
    }

    fun testJavaIntToLuaInt() {
        val i = Integer.valueOf(777)
        val v = CoerceJavaToLua.coerce(i)
        TestCase.assertEquals(LuaInteger::class.java, v.javaClass)
        TestCase.assertEquals(777, v.toint())
    }

    fun testLuaIntToJavaInt() {
        val i = LuaInteger.valueOf(777)
        var o = CoerceLuaToJava.coerce(i, Int::class.javaPrimitiveType)
        TestCase.assertEquals(Int::class.javaObjectType, o.javaClass)
        TestCase.assertEquals(777, (o as Number).toInt())
        o = CoerceLuaToJava.coerce(i, Int::class.java)
        TestCase.assertEquals(Int::class.javaObjectType, o.javaClass)
        TestCase.assertEquals(777, o)
    }

    fun testJavaStringToLuaString() {
        val s = ("777")
        val v = CoerceJavaToLua.coerce(s)
        TestCase.assertEquals(LuaString::class.java, v.javaClass)
        TestCase.assertEquals("777", v.toString())
    }

    fun testLuaStringToJavaString() {
        val s = LuaValue.valueOf("777")
        val o = CoerceLuaToJava.coerce(s, String::class.java)
        TestCase.assertEquals(String::class.java, o.javaClass)
        TestCase.assertEquals("777", o)
    }

    fun testJavaClassToLuaUserdata() {
        val va = CoerceJavaToLua.coerce(ClassA::class.java)
        val va1 = CoerceJavaToLua.coerce(ClassA::class.java)
        val vb = CoerceJavaToLua.coerce(ClassB::class.java)
        TestCase.assertSame(va, va1)
        TestCase.assertNotSame(va, vb)
        val vi = CoerceJavaToLua.coerce(ClassA())
        TestCase.assertNotSame(va, vi)
        TestCase.assertTrue(vi.isuserdata())
        TestCase.assertTrue(vi.isuserdata(ClassA::class.java))
        TestCase.assertFalse(vi.isuserdata(ClassB::class.java))
        val vj = CoerceJavaToLua.coerce(ClassB())
        TestCase.assertNotSame(vb, vj)
        TestCase.assertTrue(vj.isuserdata())
        TestCase.assertFalse(vj.isuserdata(ClassA::class.java))
        TestCase.assertTrue(vj.isuserdata(ClassB::class.java))
    }

    internal class ClassA

    internal class ClassB

    fun testJavaIntArrayToLuaTable() {
        val i = intArrayOf(222, 333)
        val v = CoerceJavaToLua.coerce(i)
        TestCase.assertEquals(JavaArray::class.java, v.javaClass)
        TestCase.assertEquals(LuaInteger.valueOf(222), v.get(ONE))
        TestCase.assertEquals(LuaInteger.valueOf(333), v.get(TWO))
        TestCase.assertEquals(TWO, v.get(LENGTH))
        TestCase.assertEquals(LuaValue.NIL, v.get(THREE))
        TestCase.assertEquals(LuaValue.NIL, v.get(ZERO))
        v.set(ONE, LuaInteger.valueOf(444))
        v.set(TWO, LuaInteger.valueOf(555))
        TestCase.assertEquals(444, i[0])
        TestCase.assertEquals(555, i[1])
        TestCase.assertEquals(LuaInteger.valueOf(444), v.get(ONE))
        TestCase.assertEquals(LuaInteger.valueOf(555), v.get(TWO))
        try {
            v.set(ZERO, LuaInteger.valueOf(777))
            TestCase.fail("array bound exception not thrown")
        } catch (lee: LuaError) {
            // expected
        }

        try {
            v.set(THREE, LuaInteger.valueOf(777))
            TestCase.fail("array bound exception not thrown")
        } catch (lee: LuaError) {
            // expected
        }

    }

    fun testLuaTableToJavaIntArray() {
        val t = LuaTable()
        t.set(1, LuaInteger.valueOf(222))
        t.set(2, LuaInteger.valueOf(333))
        var i: IntArray? = null
        val o = CoerceLuaToJava.coerce(t, IntArray::class.java)
        TestCase.assertEquals(IntArray::class.java, o.javaClass)
        i = o as IntArray
        TestCase.assertEquals(2, i.size)
        TestCase.assertEquals(222, i[0])
        TestCase.assertEquals(333, i[1])
    }

    fun testIntArrayScoringTables() {
        val a = 5
        val la = LuaInteger.valueOf(a)
        val tb = LuaTable()
        tb.set(1, la)
        val tc = LuaTable()
        tc.set(1, tb)

        val saa = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(la)
        val sab = CoerceLuaToJava.getCoercion(IntArray::class.java).score(la)
        val sac = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(la)
        TestCase.assertTrue(saa < sab)
        TestCase.assertTrue(saa < sac)
        val sba = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(tb)
        val sbb = CoerceLuaToJava.getCoercion(IntArray::class.java).score(tb)
        val sbc = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(tb)
        TestCase.assertTrue(sbb < sba)
        TestCase.assertTrue(sbb < sbc)
        val sca = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(tc)
        val scb = CoerceLuaToJava.getCoercion(IntArray::class.java).score(tc)
        val scc = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(tc)
        TestCase.assertTrue(scc < sca)
        TestCase.assertTrue(scc < scb)
    }

    fun testIntArrayScoringUserdata() {
        val a = 5
        val b = intArrayOf(44, 66)
        val c = arrayOf(intArrayOf(11, 22), intArrayOf(33, 44))
        val va = CoerceJavaToLua.coerce(a)
        val vb = CoerceJavaToLua.coerce(b)
        val vc = CoerceJavaToLua.coerce(c)

        val vaa = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(va)
        val vab = CoerceLuaToJava.getCoercion(IntArray::class.java).score(va)
        val vac = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(va)
        TestCase.assertTrue(vaa < vab)
        TestCase.assertTrue(vaa < vac)
        val vba = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(vb)
        val vbb = CoerceLuaToJava.getCoercion(IntArray::class.java).score(vb)
        val vbc = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(vb)
        TestCase.assertTrue(vbb < vba)
        TestCase.assertTrue(vbb < vbc)
        val vca = CoerceLuaToJava.getCoercion(Int::class.javaPrimitiveType).score(vc)
        val vcb = CoerceLuaToJava.getCoercion(IntArray::class.java).score(vc)
        val vcc = CoerceLuaToJava.getCoercion(Array<IntArray>::class.java).score(vc)
        TestCase.assertTrue(vcc < vca)
        TestCase.assertTrue(vcc < vcb)
    }

    class SampleClass {
        fun sample(): String {
            return "void-args"
        }

        fun sample(a: Int): String {
            return "int-args $a"
        }

        fun sample(a: IntArray): String {
            return "int-array-args " + a[0] + "," + a[1]
        }

        fun sample(a: Array<IntArray>): String {
            return "int-array-array-args " + a[0][0] + "," + a[0][1] + "," + a[1][0] + "," + a[1][1]
        }
    }

    fun testMatchVoidArgs() {
        val v = CoerceJavaToLua.coerce(SampleClass())
        val result = v.method("sample")
        TestCase.assertEquals("void-args", result.toString())
    }

    fun testMatchIntArgs() {
        val v = CoerceJavaToLua.coerce(SampleClass())
        val arg = CoerceJavaToLua.coerce(123)
        val result = v.method("sample", arg)
        TestCase.assertEquals("int-args 123", result.toString())
    }

    fun testMatchIntArrayArgs() {
        val v = CoerceJavaToLua.coerce(SampleClass())
        val arg = CoerceJavaToLua.coerce(intArrayOf(345, 678))
        val result = v.method("sample", arg)
        TestCase.assertEquals("int-array-args 345,678", result.toString())
    }

    fun testMatchIntArrayArrayArgs() {
        val v = CoerceJavaToLua.coerce(SampleClass())
        val arg = CoerceJavaToLua.coerce(arrayOf(intArrayOf(22, 33), intArrayOf(44, 55)))
        val result = v.method("sample", arg)
        TestCase.assertEquals("int-array-array-args 22,33,44,55", result.toString())
    }

    class SomeException(message: String) : RuntimeException(message)

    object SomeClass {
        @JvmStatic
        fun someMethod() {
            throw SomeException("this is some message")
        }
    }

    fun testExceptionMessage() {
        val script = "local c = luajava.bindClass( \"" + SomeClass::class.java.name + "\" )\n" +
                "return pcall( c.someMethod, c )"
        val vresult = globals!!.get("load").call(LuaValue.valueOf(script)).invoke(LuaValue.NONE)
        val status = vresult.arg1()
        val message = vresult.arg(2)
        TestCase.assertEquals(LuaValue.FALSE, status)
        val index = message.toString().indexOf("this is some message")
        TestCase.assertTrue("bad message: $message", index >= 0)
    }

    fun testLuaErrorCause() {
        val script = "luajava.bindClass( \"" + SomeClass::class.java.name + "\"):someMethod()"
        val chunk = globals!!.get("load").call(LuaValue.valueOf(script))
        try {
            chunk.invoke(LuaValue.NONE)
            TestCase.fail("call should not have succeeded")
        } catch (lee: LuaError) {
            val c = lee.getLuaCause()!!
            TestCase.assertEquals(SomeException::class.java, c::class.java)
        }

    }

    interface VarArgsInterface {
        fun varargsMethod(a: String, vararg v: String): String
        fun arrayargsMethod(a: String, v: Array<String>?): String
    }

    fun testVarArgsProxy() {
        val script = "return luajava.createProxy( \"" + VarArgsInterface::class.java.name + "\", \n" +
                "{\n" +
                "	varargsMethod = function(a,...)\n" +
                "		return table.concat({a,...},'-')\n" +
                "	end,\n" +
                "	arrayargsMethod = function(a,array)\n" +
                "		return tostring(a)..(array and \n" +
                "			('-'..tostring(array.length)\n" +
                "			..'-'..tostring(array[1])\n" +
                "			..'-'..tostring(array[2])\n" +
                "			) or '-nil')\n" +
                "	end,\n" +
                "} )\n"
        val chunk = globals!!.get("load").call(LuaValue.valueOf(script))
        if (!chunk.arg1().toboolean())
            TestCase.fail(chunk.arg(2).toString())
        val result = chunk.arg1().call()
        val u = result.touserdata()
        val v = u as VarArgsInterface
        TestCase.assertEquals("foo", v.varargsMethod("foo"))
        TestCase.assertEquals("foo-bar", v.varargsMethod("foo", "bar"))
        TestCase.assertEquals("foo-bar-etc", v.varargsMethod("foo", "bar", "etc"))
        TestCase.assertEquals("foo-0-nil-nil", v.arrayargsMethod("foo", arrayOf()))
        TestCase.assertEquals("foo-1-bar-nil", v.arrayargsMethod("foo", arrayOf("bar")))
        TestCase.assertEquals("foo-2-bar-etc", v.arrayargsMethod("foo", arrayOf("bar", "etc")))
        TestCase.assertEquals("foo-3-bar-etc", v.arrayargsMethod("foo", arrayOf("bar", "etc", "etc")))
        TestCase.assertEquals("foo-nil", v.arrayargsMethod("foo", null))
    }

    fun testBigNum() {
        val script = "bigNumA = luajava.newInstance('java.math.BigDecimal','12345678901234567890');\n" +
                "bigNumB = luajava.newInstance('java.math.BigDecimal','12345678901234567890');\n" +
                "bigNumC = bigNumA:multiply(bigNumB);\n" +
                //"print(bigNumA:toString())\n" +
                //"print(bigNumB:toString())\n" +
                //"print(bigNumC:toString())\n" +
                "return bigNumA:toString(), bigNumB:toString(), bigNumC:toString()"
        val chunk = globals!!.get("load").call(LuaValue.valueOf(script))
        if (!chunk.arg1().toboolean())
            TestCase.fail(chunk.arg(2).toString())
        val results = chunk.arg1().invoke()
        val nresults = results.narg()
        val sa = results.tojstring(1)
        val sb = results.tojstring(2)
        val sc = results.tojstring(3)
        TestCase.assertEquals(3, nresults)
        TestCase.assertEquals("12345678901234567890", sa)
        TestCase.assertEquals("12345678901234567890", sb)
        TestCase.assertEquals("152415787532388367501905199875019052100", sc)
    }

    interface IA
    interface IB : IA
    interface IC : IB

    open class A : IA
    open class B : A(), IB {

        val `object`: Any
            get() = Any()
        val string: String
            get() = "abc"
        val a: A
            get() = A()
        val b: B
            get() = B()
        val c: C
            get() = C()

        fun set(x: Any): String {
            return "set(Object) "
        }

        fun set(x: String): String {
            return "set(String) $x"
        }

        fun set(x: A): String {
            return "set(A) "
        }

        fun set(x: B): String {
            return "set(B) "
        }

        fun set(x: C): String {
            return "set(C) "
        }

        fun set(x: Byte): String {
            return "set(byte) $x"
        }

        fun set(x: Char): String {
            return "set(char) " + x.toInt()
        }

        fun set(x: Short): String {
            return "set(short) $x"
        }

        fun set(x: Int): String {
            return "set(int) $x"
        }

        fun set(x: Long): String {
            return "set(long) $x"
        }

        fun set(x: Float): String {
            return "set(float) $x"
        }

        fun set(x: Double): String {
            return "set(double) $x"
        }

        fun setr(x: Double): String {
            return "setr(double) $x"
        }

        fun setr(x: Float): String {
            return "setr(float) $x"
        }

        fun setr(x: Long): String {
            return "setr(long) $x"
        }

        fun setr(x: Int): String {
            return "setr(int) $x"
        }

        fun setr(x: Short): String {
            return "setr(short) $x"
        }

        fun setr(x: Char): String {
            return "setr(char) " + x.toInt()
        }

        fun setr(x: Byte): String {
            return "setr(byte) $x"
        }

        fun setr(x: C): String {
            return "setr(C) "
        }

        fun setr(x: B): String {
            return "setr(B) "
        }

        fun setr(x: A): String {
            return "setr(A) "
        }

        fun setr(x: String): String {
            return "setr(String) $x"
        }

        fun setr(x: Any): String {
            return "setr(Object) "
        }

        fun getbytearray(): ByteArray {
            return byteArrayOf(1, 2, 3)
        }

        fun getbyte(): Byte {
            return 1
        }

        fun getchar(): Char {
            return 65000.toChar()
        }

        fun getshort(): Short {
            return -32000
        }

        fun getint(): Int {
            return 100000
        }

        fun getlong(): Long {
            return 50000000000L
        }

        fun getfloat(): Float {
            return 6.5f
        }

        fun getdouble(): Double {
            return Math.PI
        }
    }

    open class C : B(), IC
    class D : C(), IA

    fun testOverloadedJavaMethodObject() {
        doOverloadedMethodTest("Object", "")
    }

    fun testOverloadedJavaMethodString() {
        doOverloadedMethodTest("String", "abc")
    }

    fun testOverloadedJavaMethodA() {
        doOverloadedMethodTest("A", "")
    }

    fun testOverloadedJavaMethodB() {
        doOverloadedMethodTest("B", "")
    }

    fun testOverloadedJavaMethodC() {
        doOverloadedMethodTest("C", "")
    }

    fun testOverloadedJavaMethodByte() {
        doOverloadedMethodTest("byte", "1")
    }

    fun testOverloadedJavaMethodChar() {
        doOverloadedMethodTest("char", "65000")
    }

    fun testOverloadedJavaMethodShort() {
        doOverloadedMethodTest("short", "-32000")
    }

    fun testOverloadedJavaMethodInt() {
        doOverloadedMethodTest("int", "100000")
    }

    fun testOverloadedJavaMethodLong() {
        doOverloadedMethodTest("long", "50000000000")
    }

    fun testOverloadedJavaMethodFloat() {
        doOverloadedMethodTest("float", "6.5")
    }

    fun testOverloadedJavaMethodDouble() {
        doOverloadedMethodTest("double", "3.141592653589793")
    }

    private fun doOverloadedMethodTest(typename: String, value: String) {
        val script = "local a = luajava.newInstance('" + B::class.java.name + "');\n" +
                "local b = a:set(a:get" + typename + "())\n" +
                "local c = a:setr(a:get" + typename + "())\n" +
                "return b,c"
        val chunk = globals!!.get("load").call(LuaValue.valueOf(script))
        if (!chunk.arg1().toboolean())
            TestCase.fail(chunk.arg(2).toString())
        val results = chunk.arg1().invoke()
        val nresults = results.narg()
        TestCase.assertEquals(2, nresults)
        val b = results.arg(1)
        val c = results.arg(2)
        val sb = b.tojstring()
        val sc = c.tojstring()
        TestCase.assertEquals("set($typename) $value", sb)
        TestCase.assertEquals("setr($typename) $value", sc)
    }

    fun testClassInheritanceLevels() {
        TestCase.assertEquals(0, CoerceLuaToJava.inheritanceLevels(Any::class.java, Any::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(Any::class.java, String::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(Any::class.java, A::class.java))
        TestCase.assertEquals(2, CoerceLuaToJava.inheritanceLevels(Any::class.java, B::class.java))
        TestCase.assertEquals(3, CoerceLuaToJava.inheritanceLevels(Any::class.java, C::class.java))

        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(A::class.java, Any::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(A::class.java, String::class.java)
        )
        TestCase.assertEquals(0, CoerceLuaToJava.inheritanceLevels(A::class.java, A::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(A::class.java, B::class.java))
        TestCase.assertEquals(2, CoerceLuaToJava.inheritanceLevels(A::class.java, C::class.java))

        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(B::class.java, Any::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(B::class.java, String::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(B::class.java, A::class.java)
        )
        TestCase.assertEquals(0, CoerceLuaToJava.inheritanceLevels(B::class.java, B::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(B::class.java, C::class.java))

        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(C::class.java, Any::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(C::class.java, String::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(C::class.java, A::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(C::class.java, B::class.java)
        )
        TestCase.assertEquals(0, CoerceLuaToJava.inheritanceLevels(C::class.java, C::class.java))
    }

    fun testInterfaceInheritanceLevels() {
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(IA::class.java, A::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(IB::class.java, B::class.java))
        TestCase.assertEquals(2, CoerceLuaToJava.inheritanceLevels(IA::class.java, B::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(IC::class.java, C::class.java))
        TestCase.assertEquals(2, CoerceLuaToJava.inheritanceLevels(IB::class.java, C::class.java))
        TestCase.assertEquals(3, CoerceLuaToJava.inheritanceLevels(IA::class.java, C::class.java))
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(IA::class.java, D::class.java))
        TestCase.assertEquals(2, CoerceLuaToJava.inheritanceLevels(IC::class.java, D::class.java))
        TestCase.assertEquals(3, CoerceLuaToJava.inheritanceLevels(IB::class.java, D::class.java))

        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(IB::class.java, A::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(IC::class.java, A::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(IC::class.java, B::class.java)
        )
        TestCase.assertEquals(
            CoerceLuaToJava.SCORE_UNCOERCIBLE,
            CoerceLuaToJava.inheritanceLevels(IB::class.java, IA::class.java)
        )
        TestCase.assertEquals(1, CoerceLuaToJava.inheritanceLevels(IA::class.java, IB::class.java))
    }

    fun testCoerceJavaToLuaLuaValue() {
        TestCase.assertSame(LuaValue.NIL, CoerceJavaToLua.coerce(LuaValue.NIL))
        TestCase.assertSame(LuaValue.ZERO, CoerceJavaToLua.coerce(LuaValue.ZERO))
        TestCase.assertSame(LuaValue.ONE, CoerceJavaToLua.coerce(LuaValue.ONE))
        TestCase.assertSame(LuaValue.INDEX, CoerceJavaToLua.coerce(LuaValue.INDEX))
        val table = LuaValue.tableOf()
        TestCase.assertSame(table, CoerceJavaToLua.coerce(table))
    }

    fun testCoerceJavaToLuaByeArray() {
        val bytes = "abcd".toByteArray()
        val value = CoerceJavaToLua.coerce(bytes)
        TestCase.assertEquals(LuaString::class.java, value.javaClass)
        TestCase.assertEquals(LuaValue.valueOf("abcd"), value)
    }

    companion object {

        private var globals: LuaValue? = null
        private val ZERO = LuaValue.ZERO
        private val ONE = LuaValue.ONE
        private val TWO = LuaValue.valueOf(2)
        private val THREE = LuaValue.valueOf(3)
        private val LENGTH = LuaString.valueOf("length")
    }
}

