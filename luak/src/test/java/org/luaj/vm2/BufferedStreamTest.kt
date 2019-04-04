/*******************************************************************************
 * Copyright (c) 2014 Luaj.org. All rights reserved.
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

import java.io.ByteArrayInputStream

import junit.framework.TestCase

import org.luaj.vm2.Globals.BufferedStream


class BufferedStreamTest : TestCase() {

    private fun NewBufferedStream(buflen: Int, contents: String): BufferedStream {
        return BufferedStream(buflen, ByteArrayInputStream(contents.toByteArray()))
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Throws(java.io.IOException::class)
    fun testReadEmptyStream() {
        val bs = NewBufferedStream(4, "")
        TestCase.assertEquals(-1, bs.read())
        TestCase.assertEquals(-1, bs.read(ByteArray(10)))
        TestCase.assertEquals(-1, bs.read(ByteArray(10), 0, 10))
    }

    @Throws(java.io.IOException::class)
    fun testReadByte() {
        val bs = NewBufferedStream(2, "abc")
        TestCase.assertEquals('a', bs.read().toChar())
        TestCase.assertEquals('b', bs.read().toChar())
        TestCase.assertEquals('c', bs.read().toChar())
        TestCase.assertEquals(-1, bs.read())
    }

    @Throws(java.io.IOException::class)
    fun testReadByteArray() {
        val array = ByteArray(3)
        val bs = NewBufferedStream(4, "abcdef")
        TestCase.assertEquals(3, bs.read(array))
        TestCase.assertEquals("abc", String(array))
        TestCase.assertEquals(1, bs.read(array))
        TestCase.assertEquals("d", String(array, 0, 1))
        TestCase.assertEquals(2, bs.read(array))
        TestCase.assertEquals("ef", String(array, 0, 2))
        TestCase.assertEquals(-1, bs.read())
    }

    @Throws(java.io.IOException::class)
    fun testReadByteArrayOffsetLength() {
        val array = ByteArray(10)
        val bs = NewBufferedStream(8, "abcdefghijklmn")
        TestCase.assertEquals(4, bs.read(array, 0, 4))
        TestCase.assertEquals("abcd", String(array, 0, 4))
        TestCase.assertEquals(4, bs.read(array, 2, 8))
        TestCase.assertEquals("efgh", String(array, 2, 4))
        TestCase.assertEquals(6, bs.read(array, 0, 10))
        TestCase.assertEquals("ijklmn", String(array, 0, 6))
        TestCase.assertEquals(-1, bs.read())
    }

    @Throws(java.io.IOException::class)
    fun testMarkOffsetBeginningOfStream() {
        val array = ByteArray(4)
        val bs = NewBufferedStream(8, "abcdefghijkl")
        TestCase.assertEquals(true, bs.markSupported())
        bs.mark(4)
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("abcd", String(array))
        bs.reset()
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("abcd", String(array))
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("efgh", String(array))
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("ijkl", String(array))
        TestCase.assertEquals(-1, bs.read())
    }

    @Throws(java.io.IOException::class)
    fun testMarkOffsetMiddleOfStream() {
        val array = ByteArray(4)
        val bs = NewBufferedStream(8, "abcdefghijkl")
        TestCase.assertEquals(true, bs.markSupported())
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("abcd", String(array))
        bs.mark(4)
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("efgh", String(array))
        bs.reset()
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("efgh", String(array))
        TestCase.assertEquals(4, bs.read(array))
        TestCase.assertEquals("ijkl", String(array))
        TestCase.assertEquals(-1, bs.read())
    }
}
