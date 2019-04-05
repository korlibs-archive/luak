package org.luaj.vm2.compiler


open class CompilerUnitTests : AbstractUnitTests("test/lua", "luaj3.0-tests.zip", "lua5.2.1-tests") {

    fun testAll() {
        doTest("all.lua")
    }

    fun testApi() {
        doTest("api.lua")
    }

    fun testAttrib() {
        doTest("attrib.lua")
    }

    fun testBig() {
        doTest("big.lua")
    }

    fun testBitwise() {
        doTest("bitwise.lua")
    }

    fun testCalls() {
        doTest("calls.lua")
    }

    fun testChecktable() {
        doTest("checktable.lua")
    }

    fun testClosure() {
        doTest("closure.lua")
    }

    fun testCode() {
        doTest("code.lua")
    }

    fun testConstruct() {
        doTest("constructs.lua")
    }

    fun testCoroutine() {
        doTest("coroutine.lua")
    }

    fun testDb() {
        doTest("db.lua")
    }

    fun testErrors() {
        doTest("errors.lua")
    }

    fun testEvents() {
        doTest("events.lua")
    }

    fun testFiles() {
        doTest("files.lua")
    }

    fun testGc() {
        doTest("gc.lua")
    }

    fun testGoto() {
        doTest("goto.lua")
    }

    fun testLiterals() {
        doTest("literals.lua")
    }

    fun testLocals() {
        doTest("locals.lua")
    }

    fun testMain() {
        doTest("main.lua")
    }

    fun testMath() {
        doTest("math.lua")
    }

    fun testNextvar() {
        doTest("nextvar.lua")
    }

    fun testPm() {
        doTest("pm.lua")
    }

    fun testSort() {
        doTest("sort.lua")
    }

    fun testStrings() {
        doTest("strings.lua")
    }

    fun testVararg() {
        doTest("vararg.lua")
    }

    fun testVerybig() {
        doTest("verybig.lua")
    }
}
