package org.luaj.vm2.lib.jse

import junit.framework.TestCase

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue

class LuajavaAccessibleMembersTest : TestCase() {

    private var globals: Globals? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        globals = JsePlatform.standardGlobals()
    }

    private fun invokeScript(script: String): String {
        try {
            val c = globals!!.load(script, "script")
            return c.call().tojstring()
        } catch (e: Exception) {
            TestCase.fail("exception: $e")
            return "failed"
        }

    }

    fun testAccessFromPrivateClassImplementedMethod() {
        TestCase.assertEquals(
            "privateImpl-aaa-interface_method(bar)", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "a = b:create_PrivateImpl('aaa');" +
                        "return a:interface_method('bar');"
            )
        )
    }

    fun testAccessFromPrivateClassPublicMethod() {
        TestCase.assertEquals(
            "privateImpl-aaa-public_method", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "a = b:create_PrivateImpl('aaa');" +
                        "return a:public_method();"
            )
        )
    }

    fun testAccessFromPrivateClassGetPublicField() {
        TestCase.assertEquals(
            "aaa", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "a = b:create_PrivateImpl('aaa');" +
                        "return a.public_field;"
            )
        )
    }

    fun testAccessFromPrivateClassSetPublicField() {
        TestCase.assertEquals(
            "foo", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "a = b:create_PrivateImpl('aaa');" +
                        "a.public_field = 'foo';" +
                        "return a.public_field;"
            )
        )
    }

    fun testAccessFromPrivateClassPublicConstructor() {
        TestCase.assertEquals(
            "privateImpl-constructor", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "c = b:get_PrivateImplClass();" +
                        "return luajava.new(c);"
            )
        )
    }

    fun testAccessPublicEnum() {
        TestCase.assertEquals(
            "class org.luaj.vm2.lib.jse.TestClass\$SomeEnum", invokeScript(
                "b = luajava.newInstance('" + TestClass::class.java.name + "');" +
                        "return b.SomeEnum"
            )
        )
    }
}
