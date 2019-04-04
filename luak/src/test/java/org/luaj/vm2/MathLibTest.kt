package org.luaj.vm2

import junit.framework.TestCase

import org.luaj.vm2.lib.jse.JsePlatform

class MathLibTest : TestCase() {

    private val j2se: LuaValue = JsePlatform.standardGlobals().get("math")
    //private LuaValue j2me = JmePlatform.standardGlobals().get("math");
    private var supportedOnJ2me: Boolean = false

    @Throws(Exception::class)
    override fun setUp() {
        supportedOnJ2me = true
    }

    //public void testMathDPow() {
    //	assertEquals( 1, j2mepow(2, 0), 0 );
    //	assertEquals( 2, j2mepow(2, 1), 0 );
    //	assertEquals( 8, j2mepow(2, 3), 0 );
    //	assertEquals( -8, j2mepow(-2, 3), 0 );
    //	assertEquals( 1/8., j2mepow(2, -3), 0 );
    //	assertEquals( -1/8., j2mepow(-2, -3), 0 );
    //	assertEquals( 16, j2mepow(256,  .5), 0 );
    //	assertEquals(  4, j2mepow(256, .25), 0 );
    //	assertEquals( 64, j2mepow(256, .75), 0 );
    //	assertEquals( 1./16, j2mepow(256, - .5), 0 );
    //	assertEquals( 1./ 4, j2mepow(256, -.25), 0 );
    //	assertEquals( 1./64, j2mepow(256, -.75), 0 );
    //	assertEquals( Double.NaN, j2mepow(-256,  .5), 0 );
    //	assertEquals(   1, j2mepow(.5, 0), 0 );
    //	assertEquals(  .5, j2mepow(.5, 1), 0 );
    //	assertEquals(.125, j2mepow(.5, 3), 0 );
    //	assertEquals(   2, j2mepow(.5, -1), 0 );
    //	assertEquals(   8, j2mepow(.5, -3), 0 );
    //	assertEquals(1, j2mepow(0.0625, 0), 0 );
    //	assertEquals(0.00048828125, j2mepow(0.0625, 2.75), 0 );
    //}

    //private double j2mepow(double x, double y) {
    //	return j2me.get("pow").call(LuaValue.valueOf(x),LuaValue.valueOf(y)).todouble();
    //}

    fun testAbs() {
        tryMathOp("abs", 23.45)
        tryMathOp("abs", -23.45)
    }

    fun testCos() {
        tryTrigOps("cos")
    }

    fun testCosh() {
        supportedOnJ2me = false
        tryTrigOps("cosh")
    }

    fun testDeg() {
        tryTrigOps("deg")
    }

    fun testExp() {
        //supportedOnJ2me = false;
        tryMathOp("exp", 0.0)
        tryMathOp("exp", 0.1)
        tryMathOp("exp", .9)
        tryMathOp("exp", 1.0)
        tryMathOp("exp", 9.0)
        tryMathOp("exp", -.1)
        tryMathOp("exp", -.9)
        tryMathOp("exp", -1.0)
        tryMathOp("exp", -9.0)
    }

    fun testLog() {
        supportedOnJ2me = false
        tryMathOp("log", 0.1)
        tryMathOp("log", .9)
        tryMathOp("log", 1.0)
        tryMathOp("log", 9.0)
        tryMathOp("log", -.1)
        tryMathOp("log", -.9)
        tryMathOp("log", -1.0)
        tryMathOp("log", -9.0)
    }

    fun testRad() {
        tryMathOp("rad", 0.0)
        tryMathOp("rad", 0.1)
        tryMathOp("rad", .9)
        tryMathOp("rad", 1.0)
        tryMathOp("rad", 9.0)
        tryMathOp("rad", 10.0)
        tryMathOp("rad", 100.0)
        tryMathOp("rad", -.1)
        tryMathOp("rad", -.9)
        tryMathOp("rad", -1.0)
        tryMathOp("rad", -9.0)
        tryMathOp("rad", -10.0)
        tryMathOp("rad", -100.0)
    }

    fun testSin() {
        tryTrigOps("sin")
    }

    fun testSinh() {
        supportedOnJ2me = false
        tryTrigOps("sinh")
    }

    fun testSqrt() {
        tryMathOp("sqrt", 0.0)
        tryMathOp("sqrt", 0.1)
        tryMathOp("sqrt", .9)
        tryMathOp("sqrt", 1.0)
        tryMathOp("sqrt", 9.0)
        tryMathOp("sqrt", 10.0)
        tryMathOp("sqrt", 100.0)
    }

    fun testTan() {
        tryTrigOps("tan")
    }

    fun testTanh() {
        supportedOnJ2me = false
        tryTrigOps("tanh")
    }

    fun testAtan2() {
        supportedOnJ2me = false
        tryDoubleOps("atan2", false)
    }

    fun testFmod() {
        tryDoubleOps("fmod", false)
    }

    fun testPow() {
        tryDoubleOps("pow", true)
    }

    private fun tryDoubleOps(op: String, positiveOnly: Boolean) {
        // y>0, x>0
        tryMathOp(op, 0.1, 4.0)
        tryMathOp(op, .9, 4.0)
        tryMathOp(op, 1.0, 4.0)
        tryMathOp(op, 9.0, 4.0)
        tryMathOp(op, 10.0, 4.0)
        tryMathOp(op, 100.0, 4.0)

        // y>0, x<0
        tryMathOp(op, 0.1, -4.0)
        tryMathOp(op, .9, -4.0)
        tryMathOp(op, 1.0, -4.0)
        tryMathOp(op, 9.0, -4.0)
        tryMathOp(op, 10.0, -4.0)
        tryMathOp(op, 100.0, -4.0)

        if (!positiveOnly) {
            // y<0, x>0
            tryMathOp(op, -0.1, 4.0)
            tryMathOp(op, -.9, 4.0)
            tryMathOp(op, -1.0, 4.0)
            tryMathOp(op, -9.0, 4.0)
            tryMathOp(op, -10.0, 4.0)
            tryMathOp(op, -100.0, 4.0)

            // y<0, x<0
            tryMathOp(op, -0.1, -4.0)
            tryMathOp(op, -.9, -4.0)
            tryMathOp(op, -1.0, -4.0)
            tryMathOp(op, -9.0, -4.0)
            tryMathOp(op, -10.0, -4.0)
            tryMathOp(op, -100.0, -4.0)
        }

        // degenerate cases
        tryMathOp(op, 0.0, 1.0)
        tryMathOp(op, 1.0, 0.0)
        tryMathOp(op, -1.0, 0.0)
        tryMathOp(op, 0.0, -1.0)
        tryMathOp(op, 0.0, 0.0)
    }

    private fun tryTrigOps(op: String) {
        tryMathOp(op, 0.0)
        tryMathOp(op, Math.PI / 8)
        tryMathOp(op, Math.PI * 7 / 8)
        tryMathOp(op, Math.PI * 8 / 8)
        tryMathOp(op, Math.PI * 9 / 8)
        tryMathOp(op, -Math.PI / 8)
        tryMathOp(op, -Math.PI * 7 / 8)
        tryMathOp(op, -Math.PI * 8 / 8)
        tryMathOp(op, -Math.PI * 9 / 8)
    }

    private fun tryMathOp(op: String, x: Double) {
        try {
            val expected = j2se.get(op).call(LuaValue.valueOf(x)).todouble()
            //double actual = j2me.get(op).call( LuaValue.valueOf(x)).todouble();
            //if ( supportedOnJ2me )
            //	assertEquals( expected, actual, 1.e-4 );
            //else
            //	fail("j2me should throw exception for math."+op+" but returned "+actual);
        } catch (lee: LuaError) {
            if (supportedOnJ2me)
                throw lee
        }

    }


    private fun tryMathOp(op: String, a: Double, b: Double) {
        try {
            val expected = j2se.get(op).call(LuaValue.valueOf(a), LuaValue.valueOf(b)).todouble()
            //double actual = j2me.get(op).call( LuaValue.valueOf(a), LuaValue.valueOf(b)).todouble();
            //if ( supportedOnJ2me )
            //	assertEquals( expected, actual, 1.e-5 );
            //else
            //	fail("j2me should throw exception for math."+op+" but returned "+actual);
        } catch (lee: LuaError) {
            if (supportedOnJ2me)
                throw lee
        }

    }
}
