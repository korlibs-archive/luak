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

import java.io.IOException
import java.io.InputStream

/*
/**
 * Test argument type check errors
 *
 * Results are compared for exact match with
 * the installed C-based lua environment.
 */
class ErrorsTest : ScriptDrivenTest(ScriptDrivenTest.PlatformType.JSE, dir) {

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    fun testBaseLibArgs() {
        globals.STDIN = object : InputStream() {
            @Throws(IOException::class)
            override fun read(): Int {
                return -1
            }
        }
        runTest("baselibargs")
    }

    fun testCoroutineLibArgs() {
        runTest("coroutinelibargs")
    }

    fun testDebugLibArgs() {
        runTest("debuglibargs")
    }

    fun testIoLibArgs() {
        runTest("iolibargs")
    }

    fun testMathLibArgs() {
        runTest("mathlibargs")
    }

    fun testModuleLibArgs() {
        runTest("modulelibargs")
    }

    fun testOperators() {
        runTest("operators")
    }

    fun testStringLibArgs() {
        runTest("stringlibargs")
    }

    fun testTableLibArgs() {
        runTest("tablelibargs")
    }

    companion object {

        private val dir = "errors/"
    }

}
*/
