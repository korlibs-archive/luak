package org.luaj.vm2

import junit.framework.TestCase

import org.luaj.vm2.lib.jse.JsePlatform
import org.luaj.vm2.require.RequireSampleClassCastExcep
import org.luaj.vm2.require.RequireSampleLoadLuaError
import org.luaj.vm2.require.RequireSampleLoadRuntimeExcep

class RequireClassTest : TestCase() {

    private var globals: LuaTable? = null
    private var require: LuaValue? = null

    public override fun setUp() {
        globals = JsePlatform.standardGlobals()
        require = globals!!.get("require")
    }

    fun testLoadClass() {
        val result = globals!!.load(org.luaj.vm2.require.RequireSampleSuccess())
        TestCase.assertEquals("require-sample-success-", result.tojstring())
    }

    fun testRequireClassSuccess() {
        var result = require!!.call(LuaValue.valueOf("org.luaj.vm2.require.RequireSampleSuccess"))
        TestCase.assertEquals("require-sample-success-org.luaj.vm2.require.RequireSampleSuccess", result.tojstring())
        result = require!!.call(LuaValue.valueOf("org.luaj.vm2.require.RequireSampleSuccess"))
        TestCase.assertEquals("require-sample-success-org.luaj.vm2.require.RequireSampleSuccess", result.tojstring())
    }

    fun testRequireClassLoadLuaError() {
        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleLoadLuaError::class.java.name))
            TestCase.fail("incorrectly loaded class that threw lua error")
        } catch (le: LuaError) {
            TestCase.assertEquals(
                "sample-load-lua-error",
                le.message
            )
        }

        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleLoadLuaError::class.java.name))
            TestCase.fail("incorrectly loaded class that threw lua error")
        } catch (le: LuaError) {
            TestCase.assertEquals(
                "loop or previous error loading module '" + RequireSampleLoadLuaError::class.java.name + "'",
                le.message
            )
        }

    }

    fun testRequireClassLoadRuntimeException() {
        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleLoadRuntimeExcep::class.java.name))
            TestCase.fail("incorrectly loaded class that threw runtime exception")
        } catch (le: RuntimeException) {
            TestCase.assertEquals(
                "sample-load-runtime-exception",
                le.message
            )
        }

        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleLoadRuntimeExcep::class.java.name))
            TestCase.fail("incorrectly loaded class that threw runtime exception")
        } catch (le: LuaError) {
            TestCase.assertEquals(
                "loop or previous error loading module '" + RequireSampleLoadRuntimeExcep::class.java.name + "'",
                le.message
            )
        }

    }


    fun testRequireClassClassCastException() {
        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleClassCastExcep::class.java.name))
            TestCase.fail("incorrectly loaded class that threw class cast exception")
        } catch (le: LuaError) {
            val msg = le.message
            if (msg!!.indexOf("not found") < 0)
                TestCase.fail("expected 'not found' message but got $msg")
        }

        try {
            val result = require!!.call(LuaValue.valueOf(RequireSampleClassCastExcep::class.java.name))
            TestCase.fail("incorrectly loaded class that threw class cast exception")
        } catch (le: LuaError) {
            val msg = le.message
            if (msg!!.indexOf("not found") < 0)
                TestCase.fail("expected 'not found' message but got $msg")
        }

    }
}
