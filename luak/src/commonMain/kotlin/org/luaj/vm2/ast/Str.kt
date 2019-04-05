/*******************************************************************************
 * Copyright (c) 2010 Luaj.org. All rights reserved.
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
package org.luaj.vm2.ast

import com.soywiz.kmem.*
import com.soywiz.korio.lang.*
import org.luaj.vm2.LuaString
import kotlin.jvm.*

object Str {

    @JvmStatic
    fun quoteString(image: String): LuaString {
        val s = image.substring(1, image.length - 1)
        val bytes = unquote(s)
        return LuaString.valueUsing(bytes)
    }

    @JvmStatic
    fun charString(image: String): LuaString {
        val s = image.substring(1, image.length - 1)
        val bytes = unquote(s)
        return LuaString.valueUsing(bytes)
    }

    @JvmStatic
    fun longString(image: String): LuaString {
        val i = image.indexOf('[', image.indexOf('[') + 1) + 1
        val s = image.substring(i, image.length - i)
        val b = iso88591bytes(s)
        return LuaString.valueUsing(b)
    }

    @JvmStatic
    fun iso88591bytes(s: String): ByteArray = s.toByteArray(LATIN1)

    @JvmStatic
    fun unquote(s: String): ByteArray {
        return buildByteArray {
            val baos = this
            val c = s.toCharArray()
            val n = c.size
            var i = 0
            loop@while (i < n) {
                if (c[i] == '\\' && i < n) {
                    when (c[++i]) {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                            var d = c[i++] - '0'
                            var j = 0
                            while (i < n && j < 2 && c[i] >= '0' && c[i] <= '9') {
                                d = d * 10 + (c[i] - '0')
                                i++
                                j++
                            }
                            baos.appendByte(d.toByte().toInt())
                            --i
                            i++
                            continue@loop
                        }
                        'a' -> {
                            baos.appendByte(7.toByte().toInt())
                            i++
                            continue@loop
                        }
                        'b' -> {
                            baos.appendByte('\b'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        'f' -> {
                            //baos.write('\f'.toByte().toInt())
                            baos.appendByte('\u000c'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        'n' -> {
                            baos.appendByte('\n'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        'r' -> {
                            baos.appendByte('\r'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        't' -> {
                            baos.appendByte('\t'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        'v' -> {
                            baos.appendByte(11.toByte().toInt())
                            i++
                            continue@loop
                        }
                        '"' -> {
                            baos.appendByte('"'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        '\'' -> {
                            baos.appendByte('\''.toByte().toInt())
                            i++
                            continue@loop
                        }
                        '\\' -> {
                            baos.appendByte('\\'.toByte().toInt())
                            i++
                            continue@loop
                        }
                        else -> baos.appendByte(c[i].toByte().toInt())
                    }
                } else {
                    baos.appendByte(c[i].toByte().toInt())
                }
                i++
            }
        }
    }
}
