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

import org.luaj.vm2.lib.MathLib

/**
 * Extension of [LuaNumber] which can hold a Java int as its value.
 *
 *
 * These instance are not instantiated directly by clients, but indirectly
 * via the static functions [LuaValue.valueOf] or [LuaValue.valueOf]
 * functions.  This ensures that policies regarding pooling of instances are
 * encapsulated.
 *
 *
 * There are no API's specific to LuaInteger that are useful beyond what is already
 * exposed in [LuaValue].
 *
 * @see LuaValue
 *
 * @see LuaNumber
 *
 * @see LuaDouble
 *
 * @see LuaValue.valueOf
 * @see LuaValue.valueOf
 */
class LuaInteger
/**
 * Package protected constructor.
 * @see LuaValue.valueOf
 */
internal constructor(
    /** The value being held by this instance.  */
    val v: Int
) : LuaNumber() {

    override fun isint(): Boolean {
        return true
    }

    override fun isinttype(): Boolean {
        return true
    }

    override fun islong(): Boolean {
        return true
    }

    override fun tobyte(): Byte {
        return v.toByte()
    }

    override fun tochar(): Char {
        return v.toChar()
    }

    override fun todouble(): Double {
        return v.toDouble()
    }

    override fun tofloat(): Float {
        return v.toFloat()
    }

    override fun toint(): Int {
        return v
    }

    override fun tolong(): Long {
        return v.toLong()
    }

    override fun toshort(): Short {
        return v.toShort()
    }

    override fun optdouble(defval: Double): Double {
        return v.toDouble()
    }

    override fun optint(defval: Int): Int {
        return v
    }

    override fun optinteger(defval: LuaInteger): LuaInteger? {
        return this
    }

    override fun optlong(defval: Long): Long {
        return v.toLong()
    }

    override fun tojstring(): String {
        return Integer.toString(v)
    }

    override fun strvalue(): LuaString? {
        return LuaString.valueOf(Integer.toString(v))
    }

    override fun optstring(defval: LuaString): LuaString? {
        return LuaString.valueOf(Integer.toString(v))
    }

    override fun tostring(): LuaValue {
        return LuaString.valueOf(Integer.toString(v))
    }

    override fun optjstring(defval: String): String? {
        return Integer.toString(v)
    }

    override fun checkinteger(): LuaInteger? {
        return this
    }

    override fun isstring(): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return v
    }

    // unary operators
    override fun neg(): LuaValue {
        return valueOf(-v.toLong())
    }

    // object equality, used for key comparison
    override fun equals(o: Any?): Boolean {
        return if (o is LuaInteger) o.v == v else false
    }

    // equality w/ metatable processing
    override fun eq(`val`: LuaValue): LuaValue {
        return if (`val`.raweq(v)) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun eq_b(`val`: LuaValue): Boolean {
        return `val`.raweq(v)
    }

    // equality w/o metatable processing
    override fun raweq(`val`: LuaValue): Boolean {
        return `val`.raweq(v)
    }

    override fun raweq(`val`: Double): Boolean {
        return v.toDouble() == `val`
    }

    override fun raweq(`val`: Int): Boolean {
        return v == `val`
    }

    // arithmetic operators
    override fun add(rhs: LuaValue): LuaValue {
        return rhs.add(v)
    }

    override fun add(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs + v)
    }

    override fun add(lhs: Int): LuaValue {
        return LuaInteger.valueOf(lhs + v.toLong())
    }

    override fun sub(rhs: LuaValue): LuaValue {
        return rhs.subFrom(v)
    }

    override fun sub(rhs: Double): LuaValue {
        return LuaDouble.valueOf(v - rhs)
    }

    override fun sub(rhs: Int): LuaValue {
        return LuaDouble.valueOf((v - rhs).toDouble())
    }

    override fun subFrom(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs - v)
    }

    override fun subFrom(lhs: Int): LuaValue {
        return LuaInteger.valueOf(lhs - v.toLong())
    }

    override fun mul(rhs: LuaValue): LuaValue {
        return rhs.mul(v)
    }

    override fun mul(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs * v)
    }

    override fun mul(lhs: Int): LuaValue {
        return LuaInteger.valueOf(lhs * v.toLong())
    }

    override fun pow(rhs: LuaValue): LuaValue {
        return rhs.powWith(v)
    }

    override fun pow(rhs: Double): LuaValue {
        return MathLib.dpow(v.toDouble(), rhs)
    }

    override fun pow(rhs: Int): LuaValue {
        return MathLib.dpow(v.toDouble(), rhs.toDouble())
    }

    override fun powWith(lhs: Double): LuaValue {
        return MathLib.dpow(lhs, v.toDouble())
    }

    override fun powWith(lhs: Int): LuaValue {
        return MathLib.dpow(lhs.toDouble(), v.toDouble())
    }

    override fun div(rhs: LuaValue): LuaValue {
        return rhs.divInto(v.toDouble())
    }

    override fun div(rhs: Double): LuaValue {
        return LuaDouble.ddiv(v.toDouble(), rhs)
    }

    override fun div(rhs: Int): LuaValue {
        return LuaDouble.ddiv(v.toDouble(), rhs.toDouble())
    }

    override fun divInto(lhs: Double): LuaValue {
        return LuaDouble.ddiv(lhs, v.toDouble())
    }

    override fun mod(rhs: LuaValue): LuaValue {
        return rhs.modFrom(v.toDouble())
    }

    override fun mod(rhs: Double): LuaValue {
        return LuaDouble.dmod(v.toDouble(), rhs)
    }

    override fun mod(rhs: Int): LuaValue {
        return LuaDouble.dmod(v.toDouble(), rhs.toDouble())
    }

    override fun modFrom(lhs: Double): LuaValue {
        return LuaDouble.dmod(lhs, v.toDouble())
    }

    // relational operators
    override fun lt(rhs: LuaValue): LuaValue {
        return if (rhs.gt_b(v)) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lt(rhs: Double): LuaValue {
        return if (v < rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lt(rhs: Int): LuaValue {
        return if (v < rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lt_b(rhs: LuaValue): Boolean {
        return rhs.gt_b(v)
    }

    override fun lt_b(rhs: Int): Boolean {
        return v < rhs
    }

    override fun lt_b(rhs: Double): Boolean {
        return v < rhs
    }

    override fun lteq(rhs: LuaValue): LuaValue {
        return if (rhs.gteq_b(v)) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lteq(rhs: Double): LuaValue {
        return if (v <= rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lteq(rhs: Int): LuaValue {
        return if (v <= rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun lteq_b(rhs: LuaValue): Boolean {
        return rhs.gteq_b(v)
    }

    override fun lteq_b(rhs: Int): Boolean {
        return v <= rhs
    }

    override fun lteq_b(rhs: Double): Boolean {
        return v <= rhs
    }

    override fun gt(rhs: LuaValue): LuaValue {
        return if (rhs.lt_b(v)) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gt(rhs: Double): LuaValue {
        return if (v > rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gt(rhs: Int): LuaValue {
        return if (v > rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gt_b(rhs: LuaValue): Boolean {
        return rhs.lt_b(v)
    }

    override fun gt_b(rhs: Int): Boolean {
        return v > rhs
    }

    override fun gt_b(rhs: Double): Boolean {
        return v > rhs
    }

    override fun gteq(rhs: LuaValue): LuaValue {
        return if (rhs.lteq_b(v)) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gteq(rhs: Double): LuaValue {
        return if (v >= rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gteq(rhs: Int): LuaValue {
        return if (v >= rhs) LuaValue.TRUE else LuaValue.FALSE
    }

    override fun gteq_b(rhs: LuaValue): Boolean {
        return rhs.lteq_b(v)
    }

    override fun gteq_b(rhs: Int): Boolean {
        return v >= rhs
    }

    override fun gteq_b(rhs: Double): Boolean {
        return v >= rhs
    }

    // string comparison
    override fun strcmp(rhs: LuaString): Int {
        typerror("attempt to compare number with string")
        return 0
    }

    override fun checkint(): Int {
        return v
    }

    override fun checklong(): Long {
        return v.toLong()
    }

    override fun checkdouble(): Double {
        return v.toDouble()
    }

    override fun checkjstring(): String? {
        return v.toString()
    }

    override fun checkstring(): LuaString? {
        return LuaValue.valueOf(v.toString())
    }

    companion object {

        private val intValues = Array(512) { LuaInteger(it - 256) }

        @JvmName("valueOf2")
        @JvmStatic
        fun valueOf(i: Int): LuaInteger {
            return if (i <= 255 && i >= -256) intValues[i + 256]!! else LuaInteger(i)
        }

        // TODO consider moving this to LuaValue
        /** Return a LuaNumber that represents the value provided
         * @param l long value to represent.
         * @return LuaNumber that is eithe LuaInteger or LuaDouble representing l
         * @see LuaValue.valueOf
         * @see LuaValue.valueOf
         */
        @JvmStatic
        fun valueOf(l: Long): LuaNumber {
            val i = l.toInt()
            return if (l == i.toLong())
                if (i <= 255 && i >= -256)
                    intValues[i + 256]
                else
                    LuaInteger(i)
            else
                LuaDouble.valueOf(l.toDouble())
        }

        @JvmStatic
        fun hashCode(x: Int): Int {
            return x
        }
    }

}
