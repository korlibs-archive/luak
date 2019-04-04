package org.luaj.vm2.lib.jse

import junit.framework.TestCase

import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue

class LuajavaClassMembersTest : TestCase() {
    open class A
    open class B : A {
        @JvmField
        var m_byte_field: Byte = 0
        @JvmField
        var m_int_field: Int = 0
        @JvmField
        var m_double_field: Double = 0.toDouble()
        @JvmField
        var m_string_field: String? = null

        fun getString(): String = "abc"

        constructor() {}
        constructor(i: Int) {
            m_int_field = i
        }

        fun setString(x: String): String {
            return "setString(String) $x"
        }

        open fun getint(): Int {
            return 100000
        }

        fun uniq(): String {
            return "uniq()"
        }

        fun uniqs(s: String): String {
            return "uniqs(string:$s)"
        }

        fun uniqi(i: Int): String {
            return "uniqi(int:$i)"
        }

        fun uniqsi(s: String, i: Int): String {
            return "uniqsi(string:$s,int:$i)"
        }

        fun uniqis(i: Int, s: String): String {
            return "uniqis(int:$i,string:$s)"
        }

        fun pick(): String {
            return "pick()"
        }

        open fun pick(s: String): String {
            return "pick(string:$s)"
        }

        open fun pick(i: Int): String {
            return "pick(int:$i)"
        }

        fun pick(s: String, i: Int): String {
            return "pick(string:$s,int:$i)"
        }

        fun pick(i: Int, s: String): String {
            return "pick(int:$i,string:$s)"
        }

        companion object {

            @JvmStatic
            fun staticpick(): String {
                return "static-pick()"
            }

            @JvmStatic
            fun staticpick(s: String): String {
                return "static-pick(string:$s)"
            }

            @JvmStatic
            fun staticpick(i: Int): String {
                return "static-pick(int:$i)"
            }

            @JvmStatic
            fun staticpick(s: String, i: Int): String {
                return "static-pick(string:$s,int:$i)"
            }

            @JvmStatic
            fun staticpick(i: Int, s: String): String {
                return "static-pick(int:$i,string:$s)"
            }
        }
    }

    class C : B {
        constructor() {}
        constructor(s: String) {
            m_string_field = s
        }

        constructor(i: Int) {
            m_int_field = i
        }

        constructor(s: String, i: Int) {
            m_string_field = s
            m_int_field = i
        }

        override fun getint(): Int {
            return 200000
        }

        override fun pick(s: String): String {
            return "class-c-pick(string:$s)"
        }

        override fun pick(i: Int): String {
            return "class-c-pick(int:$i)"
        }

        object D {
            @JvmStatic
            fun name(): String {
                return "name-of-D"
            }
        }
    }

    fun testSetByteField() {
        val b = B()
        val i = JavaInstance(b)
        i.set("m_byte_field", ONE)
        TestCase.assertEquals(1, b.m_byte_field.toInt())
        TestCase.assertEquals(ONE, i.get("m_byte_field"))
        i.set("m_byte_field", PI)
        TestCase.assertEquals(3, b.m_byte_field.toInt())
        TestCase.assertEquals(THREE, i.get("m_byte_field"))
        i.set("m_byte_field", ABC)
        TestCase.assertEquals(0, b.m_byte_field.toInt())
        TestCase.assertEquals(ZERO, i.get("m_byte_field"))
    }

    fun testSetDoubleField() {
        val b = B()
        val i = JavaInstance(b)
        i.set("m_double_field", ONE)
        TestCase.assertEquals(1.0, b.m_double_field)
        TestCase.assertEquals(ONE, i.get("m_double_field"))
        i.set("m_double_field", PI)
        TestCase.assertEquals(Math.PI, b.m_double_field)
        TestCase.assertEquals(PI, i.get("m_double_field"))
        i.set("m_double_field", ABC)
        TestCase.assertEquals(0.0, b.m_double_field)
        TestCase.assertEquals(ZERO, i.get("m_double_field"))
    }

    fun testNoFactory() {
        val c = JavaClass.forClass(A::class.java)
        try {
            c.call()
            TestCase.fail("did not throw lua error as expected")
        } catch (e: LuaError) {
        }

    }

    fun testUniqueFactoryCoercible() {
        val c = JavaClass.forClass(B::class.java)
        TestCase.assertEquals(JavaClass::class.java, c.javaClass)
        val constr = c.get("new")
        TestCase.assertEquals(JavaConstructor.Overload::class.java, constr::class.java)
        val v = constr.call(NUMS)
        val b = v.touserdata()
        TestCase.assertEquals(B::class.java, b!!.javaClass)
        TestCase.assertEquals(123, (b as B).m_int_field)
        val b0 = constr.call().touserdata()
        TestCase.assertEquals(B::class.java, b0!!.javaClass)
        TestCase.assertEquals(0, (b0 as B).m_int_field)
    }

    fun testUniqueFactoryUncoercible() {
        val f = JavaClass.forClass(B::class.java)
        val constr = f.get("new")
        TestCase.assertEquals(JavaConstructor.Overload::class.java, constr::class.java)
        try {
            val v = constr.call(LuaValue.userdataOf(Any()))
            val b = v.touserdata()
            // fail( "did not throw lua error as expected" );
            TestCase.assertEquals(0, (b as B).m_int_field)
        } catch (e: LuaError) {
        }

    }

    fun testOverloadedFactoryCoercible() {
        val f = JavaClass.forClass(C::class.java)
        val constr = f.get("new")
        TestCase.assertEquals(JavaConstructor.Overload::class.java, constr.javaClass)
        val c = constr.call().touserdata()
        val ci = constr.call(LuaValue.valueOf(123)).touserdata()
        val cs = constr.call(LuaValue.valueOf("abc")).touserdata()
        val csi = constr.call(LuaValue.valueOf("def"), LuaValue.valueOf(456)).touserdata()
        TestCase.assertEquals(C::class.java, c!!.javaClass)
        TestCase.assertEquals(C::class.java, ci!!.javaClass)
        TestCase.assertEquals(C::class.java, cs!!.javaClass)
        TestCase.assertEquals(C::class.java, csi!!.javaClass)
        TestCase.assertEquals(null, (c as C).m_string_field)
        TestCase.assertEquals(0, c.m_int_field)
        TestCase.assertEquals("abc", (cs as C).m_string_field)
        TestCase.assertEquals(0, cs.m_int_field)
        TestCase.assertEquals(null, (ci as C).m_string_field)
        TestCase.assertEquals(123, ci.m_int_field)
        TestCase.assertEquals("def", (csi as C).m_string_field)
        TestCase.assertEquals(456, csi.m_int_field)
    }

    fun testOverloadedFactoryUncoercible() {
        val f = JavaClass.forClass(C::class.java)
        try {
            val c = f.call(LuaValue.userdataOf(Any()))
            // fail( "did not throw lua error as expected" );
            TestCase.assertEquals(0, (c as C).m_int_field)
            TestCase.assertEquals(null, (c as C).m_string_field)
        } catch (e: LuaError) {
        }

    }

    fun testNoAttribute() {
        val f = JavaClass.forClass(A::class.java)
        val v = f.get("bogus")
        TestCase.assertEquals(v, LuaValue.NIL)
        try {
            f.set("bogus", ONE)
            TestCase.fail("did not throw lua error as expected")
        } catch (e: LuaError) {
        }

    }

    fun testFieldAttributeCoercible() {
        var i = JavaInstance(B())
        i.set("m_int_field", ONE)
        TestCase.assertEquals(1, i.get("m_int_field").toint())
        i.set("m_int_field", THREE)
        TestCase.assertEquals(3, i.get("m_int_field").toint())
        i = JavaInstance(C())
        i.set("m_int_field", ONE)
        TestCase.assertEquals(1, i.get("m_int_field").toint())
        i.set("m_int_field", THREE)
        TestCase.assertEquals(3, i.get("m_int_field").toint())
    }

    fun testUniqueMethodAttributeCoercible() {
        val b = B()
        val ib = JavaInstance(b)
        val b_getString = ib.get("getString")
        val b_getint = ib.get("getint")
        TestCase.assertEquals(JavaMethod::class.java, b_getString.javaClass)
        TestCase.assertEquals(JavaMethod::class.java, b_getint.javaClass)
        TestCase.assertEquals("abc", b_getString.call(SOMEB).tojstring())
        TestCase.assertEquals(100000, b_getint.call(SOMEB).toint())
        TestCase.assertEquals("abc", b_getString.call(SOMEC).tojstring())
        TestCase.assertEquals(200000, b_getint.call(SOMEC).toint())
    }

    fun testUniqueMethodAttributeArgsCoercible() {
        val b = B()
        val ib = JavaInstance(b)
        val uniq = ib.get("uniq")
        val uniqs = ib.get("uniqs")
        val uniqi = ib.get("uniqi")
        val uniqsi = ib.get("uniqsi")
        val uniqis = ib.get("uniqis")
        TestCase.assertEquals(JavaMethod::class.java, uniq.javaClass)
        TestCase.assertEquals(JavaMethod::class.java, uniqs.javaClass)
        TestCase.assertEquals(JavaMethod::class.java, uniqi.javaClass)
        TestCase.assertEquals(JavaMethod::class.java, uniqsi.javaClass)
        TestCase.assertEquals(JavaMethod::class.java, uniqis.javaClass)
        TestCase.assertEquals("uniq()", uniq.call(SOMEB).tojstring())
        TestCase.assertEquals("uniqs(string:abc)", uniqs.call(SOMEB, ABC).tojstring())
        TestCase.assertEquals("uniqi(int:1)", uniqi.call(SOMEB, ONE).tojstring())
        TestCase.assertEquals("uniqsi(string:abc,int:1)", uniqsi.call(SOMEB, ABC, ONE).tojstring())
        TestCase.assertEquals("uniqis(int:1,string:abc)", uniqis.call(SOMEB, ONE, ABC).tojstring())
        TestCase.assertEquals(
            "uniqis(int:1,string:abc)",
            uniqis.invoke(LuaValue.varargsOf(arrayOf(SOMEB, ONE, ABC, ONE))).arg1().tojstring()
        )
    }

    fun testOverloadedMethodAttributeCoercible() {
        val b = B()
        val ib = JavaInstance(b)
        val p = ib.get("pick")
        TestCase.assertEquals("pick()", p.call(SOMEB).tojstring())
        TestCase.assertEquals("pick(string:abc)", p.call(SOMEB, ABC).tojstring())
        TestCase.assertEquals("pick(int:1)", p.call(SOMEB, ONE).tojstring())
        TestCase.assertEquals("pick(string:abc,int:1)", p.call(SOMEB, ABC, ONE).tojstring())
        TestCase.assertEquals("pick(int:1,string:abc)", p.call(SOMEB, ONE, ABC).tojstring())
        TestCase.assertEquals(
            "pick(int:1,string:abc)",
            p.invoke(LuaValue.varargsOf(arrayOf(SOMEB, ONE, ABC, ONE))).arg1().tojstring()
        )
    }

    fun testUnboundOverloadedMethodAttributeCoercible() {
        val b = B()
        val ib = JavaInstance(b)
        val p = ib.get("pick")
        TestCase.assertEquals(JavaMethod.Overload::class.java, p.javaClass)
        TestCase.assertEquals("pick()", p.call(SOMEC).tojstring())
        TestCase.assertEquals("class-c-pick(string:abc)", p.call(SOMEC, ABC).tojstring())
        TestCase.assertEquals("class-c-pick(int:1)", p.call(SOMEC, ONE).tojstring())
        TestCase.assertEquals("pick(string:abc,int:1)", p.call(SOMEC, ABC, ONE).tojstring())
        TestCase.assertEquals("pick(int:1,string:abc)", p.call(SOMEC, ONE, ABC).tojstring())
        TestCase.assertEquals(
            "pick(int:1,string:abc)",
            p.invoke(LuaValue.varargsOf(arrayOf(SOMEC, ONE, ABC, ONE))).arg1().tojstring()
        )
    }

    fun testOverloadedStaticMethodAttributeCoercible() {
        val b = B()
        val ib = JavaInstance(b)
        val p = ib.get("staticpick")
        TestCase.assertEquals("static-pick()", p.call(SOMEB).tojstring())
        TestCase.assertEquals("static-pick(string:abc)", p.call(SOMEB, ABC).tojstring())
        TestCase.assertEquals("static-pick(int:1)", p.call(SOMEB, ONE).tojstring())
        TestCase.assertEquals("static-pick(string:abc,int:1)", p.call(SOMEB, ABC, ONE).tojstring())
        TestCase.assertEquals("static-pick(int:1,string:abc)", p.call(SOMEB, ONE, ABC).tojstring())
        TestCase.assertEquals(
            "static-pick(int:1,string:abc)",
            p.invoke(LuaValue.varargsOf(arrayOf(SOMEB, ONE, ABC, ONE))).arg1().tojstring()
        )
    }

    fun testGetInnerClass() {
        val c = C()
        val ic = JavaInstance(c)
        val d = ic.get("D")
        TestCase.assertFalse(d.isnil())
        TestCase.assertSame(d, JavaClass.forClass(C.D::class.java))
        val e = ic.get("E")
        TestCase.assertTrue(e.isnil())
    }

    companion object {

        internal var ZERO: LuaValue = LuaValue.ZERO
        internal var ONE: LuaValue = LuaValue.ONE
        internal var PI: LuaValue = LuaValue.valueOf(Math.PI)
        internal var THREE: LuaValue = LuaValue.valueOf(3)
        internal var NUMS: LuaValue = LuaValue.valueOf(123)
        internal var ABC: LuaValue = LuaValue.valueOf("abc")
        internal var SOMEA = CoerceJavaToLua.coerce(A())
        internal var SOMEB = CoerceJavaToLua.coerce(B())
        internal var SOMEC = CoerceJavaToLua.coerce(C())
    }
}
