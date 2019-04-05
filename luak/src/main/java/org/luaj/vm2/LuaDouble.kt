/*******************************************************************************
 * Copyright (c) 2009-2011 Luaj.org. All rights reserved.
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
import kotlin.math.floor

/**
 * Extension of [LuaNumber] which can hold a Java double as its value.
 *
 *
 * These instance are not instantiated directly by clients, but indirectly
 * via the static functions [LuaValue.valueOf] or [LuaValue.valueOf]
 * functions.  This ensures that values which can be represented as int
 * are wrapped in [LuaInteger] instead of [LuaDouble].
 *
 *
 * Almost all API's implemented in LuaDouble are defined and documented in [LuaValue].
 *
 *
 * However the constants [.NAN], [.POSINF], [.NEGINF],
 * [.JSTR_NAN], [.JSTR_POSINF], and [.JSTR_NEGINF] may be useful
 * when dealing with Nan or Infinite values.
 *
 *
 * LuaDouble also defines functions for handling the unique math rules of lua devision and modulo in
 *
 *  * [.ddiv]
 *  * [.ddiv_d]
 *  * [.dmod]
 *  * [.dmod_d]
 *
 *
 *
 * @see LuaValue
 *
 * @see LuaNumber
 *
 * @see LuaInteger
 *
 * @see LuaValue.valueOf
 * @see LuaValue.valueOf
 */
class LuaDouble
/** Don't allow ints to be boxed by DoubleValues   */
private constructor(
    /** The value being held by this instance.  */
    internal val v: Double
) : LuaNumber() {

    override fun hashCode(): Int {
        val l = (v + 1).toRawBits()
        return (l shr 32).toInt() + l.toInt()
    }

    override fun islong(): Boolean {
        return v == v.toLong().toDouble()
    }

    override fun tobyte(): Byte {
        return v.toLong().toByte()
    }

    override fun tochar(): Char {
        return v.toLong().toChar()
    }

    override fun todouble(): Double {
        return v
    }

    override fun tofloat(): Float {
        return v.toFloat()
    }

    override fun toint(): Int {
        return v.toLong().toInt()
    }

    override fun tolong(): Long {
        return v.toLong()
    }

    override fun toshort(): Short {
        return v.toLong().toShort()
    }

    override fun optdouble(defval: Double): Double {
        return v
    }

    override fun optint(defval: Int): Int {
        return v.toLong().toInt()
    }

    override fun optinteger(defval: LuaInteger?): LuaInteger? {
        return LuaInteger.valueOf(v.toLong().toInt())
    }

    override fun optlong(defval: Long): Long {
        return v.toLong()
    }

    override fun checkinteger(): LuaInteger? {
        return LuaInteger.valueOf(v.toLong().toInt())
    }

    // unary operators
    override fun neg(): LuaValue {
        return valueOf(-v)
    }

    // object equality, used for key comparison
    override fun equals(o: Any?): Boolean {
        return if (o is LuaDouble) o.v == v else false
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
        return v == `val`
    }

    override fun raweq(`val`: Int): Boolean {
        return v == `val`.toDouble()
    }

    // basic binary arithmetic
    override fun add(rhs: LuaValue): LuaValue {
        return rhs.add(v)
    }

    override fun add(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs + v)
    }

    override fun sub(rhs: LuaValue): LuaValue {
        return rhs.subFrom(v)
    }

    override fun sub(rhs: Double): LuaValue {
        return LuaDouble.valueOf(v - rhs)
    }

    override fun sub(rhs: Int): LuaValue {
        return LuaDouble.valueOf(v - rhs)
    }

    override fun subFrom(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs - v)
    }

    override fun mul(rhs: LuaValue): LuaValue {
        return rhs.mul(v)
    }

    override fun mul(lhs: Double): LuaValue {
        return LuaDouble.valueOf(lhs * v)
    }

    override fun mul(lhs: Int): LuaValue {
        return LuaDouble.valueOf(lhs * v)
    }

    override fun pow(rhs: LuaValue): LuaValue {
        return rhs.powWith(v)
    }

    override fun pow(rhs: Double): LuaValue {
        return MathLib.dpow(v, rhs)
    }

    override fun pow(rhs: Int): LuaValue {
        return MathLib.dpow(v, rhs.toDouble())
    }

    override fun powWith(lhs: Double): LuaValue {
        return MathLib.dpow(lhs, v)
    }

    override fun powWith(lhs: Int): LuaValue {
        return MathLib.dpow(lhs.toDouble(), v)
    }

    override fun div(rhs: LuaValue): LuaValue {
        return rhs.divInto(v)
    }

    override fun div(rhs: Double): LuaValue {
        return LuaDouble.ddiv(v, rhs)
    }

    override fun div(rhs: Int): LuaValue {
        return LuaDouble.ddiv(v, rhs.toDouble())
    }

    override fun divInto(lhs: Double): LuaValue {
        return LuaDouble.ddiv(lhs, v)
    }

    override fun mod(rhs: LuaValue): LuaValue {
        return rhs.modFrom(v)
    }

    override fun mod(rhs: Double): LuaValue {
        return LuaDouble.dmod(v, rhs)
    }

    override fun mod(rhs: Int): LuaValue {
        return LuaDouble.dmod(v, rhs.toDouble())
    }

    override fun modFrom(lhs: Double): LuaValue {
        return LuaDouble.dmod(lhs, v)
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

    override fun tojstring(): String {
        /*
		if ( v == 0.0 ) { // never occurs in J2me
			long bits = Double.doubleToLongBits( v );
			return ( bits >> 63 == 0 ) ? "0" : "-0";
		}
		*/
        val l = v.toLong()
        if (l.toDouble() == v)
            return l.toString()
        if (v.isNaN())
            return JSTR_NAN
        return if ((v).isInfinite()) if (v < 0) JSTR_NEGINF else JSTR_POSINF else v.toFloat().toString()
    }

    override fun strvalue(): LuaString? {
        return LuaString.valueOf(tojstring())
    }

    override fun optstring(defval: LuaString?): LuaString? {
        return LuaString.valueOf(tojstring())
    }

    override fun tostring(): LuaValue {
        return LuaString.valueOf(tojstring())
    }

    override fun optjstring(defval: String?): String? {
        return tojstring()
    }

    override fun optnumber(defval: LuaNumber?): LuaNumber? {
        return this
    }

    override fun isnumber(): Boolean {
        return true
    }

    override fun isstring(): Boolean {
        return true
    }

    override fun tonumber(): LuaValue {
        return this
    }

    override fun checkint(): Int {
        return v.toLong().toInt()
    }

    override fun checklong(): Long {
        return v.toLong()
    }

    override fun checknumber(): LuaNumber? {
        return this
    }

    override fun checkdouble(): Double {
        return v
    }

    override fun checkjstring(): String? {
        return tojstring()
    }

    override fun checkstring(): LuaString {
        return LuaString.valueOf(tojstring())
    }

    override fun isvalidkey(): Boolean {
        return !(v.isNaN())
    }

    companion object {

        /** Constant LuaDouble representing NaN (not a number)  */
        @JvmField val NAN = LuaDouble(Double.NaN)

        /** Constant LuaDouble representing positive infinity  */
        @JvmField val POSINF = LuaDouble(Double.POSITIVE_INFINITY)

        /** Constant LuaDouble representing negative infinity  */
        @JvmField val NEGINF = LuaDouble(Double.NEGATIVE_INFINITY)

        /** Constant String representation for NaN (not a number), "nan"  */
        @JvmField val JSTR_NAN = "nan"

        /** Constant String representation for positive infinity, "inf"  */
        @JvmField val JSTR_POSINF = "inf"

        /** Constant String representation for negative infinity, "-inf"  */
        @JvmField val JSTR_NEGINF = "-inf"

        @JvmName("valueOf2")
        @JvmStatic fun valueOf(d: Double): LuaNumber {
            val id = d.toInt()
            return if (d == id.toDouble()) LuaInteger.valueOf(id) as LuaNumber else LuaDouble(d)
        }


        /** Divide two double numbers according to lua math, and return a [LuaValue] result.
         * @param lhs Left-hand-side of the division.
         * @param rhs Right-hand-side of the division.
         * @return [LuaValue] for the result of the division,
         * taking into account positive and negiative infinity, and Nan
         * @see .ddiv_d
         */
        @JvmStatic fun ddiv(lhs: Double, rhs: Double): LuaValue {
            return if (rhs != 0.0) valueOf(lhs / rhs) else if (lhs > 0) POSINF else if (lhs == 0.0) NAN else NEGINF
        }

        /** Divide two double numbers according to lua math, and return a double result.
         * @param lhs Left-hand-side of the division.
         * @param rhs Right-hand-side of the division.
         * @return Value of the division, taking into account positive and negative infinity, and Nan
         * @see .ddiv
         */
        @JvmStatic fun ddiv_d(lhs: Double, rhs: Double): Double {
            return when {
                rhs != 0.0 -> lhs / rhs
                lhs > 0 -> Double.POSITIVE_INFINITY
                lhs == 0.0 -> Double.NaN
                else -> Double.NEGATIVE_INFINITY
            }
        }

        /** Take modulo double numbers according to lua math, and return a [LuaValue] result.
         * @param lhs Left-hand-side of the modulo.
         * @param rhs Right-hand-side of the modulo.
         * @return [LuaValue] for the result of the modulo,
         * using lua's rules for modulo
         * @see .dmod_d
         */
        @JvmStatic fun dmod(lhs: Double, rhs: Double): LuaValue {
            return if (rhs != 0.0) valueOf(lhs - rhs * Math.floor(lhs / rhs)) else NAN
        }

        /** Take modulo for double numbers according to lua math, and return a double result.
         * @param lhs Left-hand-side of the modulo.
         * @param rhs Right-hand-side of the modulo.
         * @return double value for the result of the modulo,
         * using lua's rules for modulo
         * @see .dmod
         */
        @JvmStatic fun dmod_d(lhs: Double, rhs: Double): Double {
            return when {
                rhs != 0.0 -> lhs - rhs * kotlin.math.floor(lhs / rhs)
                else -> Double.NaN
            }
        }
    }
}
