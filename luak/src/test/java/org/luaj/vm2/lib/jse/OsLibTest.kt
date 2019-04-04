package org.luaj.vm2.lib.jse

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OsLib

import junit.framework.TestCase

class OsLibTest : TestCase() {

    //LuaValue jme_lib;
    internal lateinit var jse_lib: LuaValue
    internal var time: Double = 0.toDouble()

    public override fun setUp() {
        jse_lib = JsePlatform.standardGlobals().get("os")
        //jme_lib = JmePlatform.standardGlobals().get("os");;
        time = java.util.Date(2001 - 1900, 7, 23, 14, 55, 2).time / 1000.0
    }

    internal fun t(format: String, expected: String) {
        //String actual = jme_lib.get("date").call(LuaValue.valueOf(format), LuaValue.valueOf(time)).tojstring();
        //assertEquals(expected, actual);
    }

    fun testStringDateChars() {
        t("foo", "foo")
    }

    fun testStringDate_a() {
        t("%a", "Thu")
    }

    fun testStringDate_A() {
        t("%A", "Thursday")
    }

    fun testStringDate_b() {
        t("%b", "Aug")
    }

    fun testStringDate_B() {
        t("%B", "August")
    }

    fun testStringDate_c() {
        t("%c", "Thu Aug 23 14:55:02 2001")
    }

    fun testStringDate_d() {
        t("%d", "23")
    }

    fun testStringDate_H() {
        t("%H", "14")
    }

    fun testStringDate_I() {
        t("%I", "02")
    }

    fun testStringDate_j() {
        t("%j", "235")
    }

    fun testStringDate_m() {
        t("%m", "08")
    }

    fun testStringDate_M() {
        t("%M", "55")
    }

    fun testStringDate_p() {
        t("%p", "PM")
    }

    fun testStringDate_S() {
        t("%S", "02")
    }

    fun testStringDate_U() {
        t("%U", "33")
    }

    fun testStringDate_w() {
        t("%w", "4")
    }

    fun testStringDate_W() {
        t("%W", "34")
    }

    fun testStringDate_x() {
        t("%x", "08/23/01")
    }

    fun testStringDate_X() {
        t("%X", "14:55:02")
    }

    fun testStringDate_y() {
        t("%y", "01")
    }

    fun testStringDate_Y() {
        t("%Y", "2001")
    }

    fun testStringDate_Pct() {
        t("%%", "%")
    }

    fun testStringDate_UW_neg4() {
        time -= 4 * DAY
        t("%c %U %W", "Sun Aug 19 14:55:02 2001 33 33")
    }

    fun testStringDate_UW_neg3() {
        time -= 3 * DAY
        t("%c %U %W", "Mon Aug 20 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_neg2() {
        time -= 2 * DAY
        t("%c %U %W", "Tue Aug 21 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_neg1() {
        time -= DAY
        t("%c %U %W", "Wed Aug 22 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_pos0() {
        time += 0.0
        t("%c %U %W", "Thu Aug 23 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_pos1() {
        time += DAY
        t("%c %U %W", "Fri Aug 24 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_pos2() {
        time += 2 * DAY
        t("%c %U %W", "Sat Aug 25 14:55:02 2001 33 34")
    }

    fun testStringDate_UW_pos3() {
        time += 3 * DAY
        t("%c %U %W", "Sun Aug 26 14:55:02 2001 34 34")
    }

    fun testStringDate_UW_pos4() {
        time += 4 * DAY
        t("%c %U %W", "Mon Aug 27 14:55:02 2001 34 35")
    }

    fun testJseOsGetenvForEnvVariables() {
        val USER = LuaValue.valueOf("USER")
        val jse_user = jse_lib.get("getenv").call(USER)
        //LuaValue jme_user = jme_lib.get("getenv").call(USER);
        TestCase.assertFalse(jse_user.isnil())
        //assertTrue(jme_user.isnil());
        println("User: $jse_user")
    }

    fun testJseOsGetenvForSystemProperties() {
        System.setProperty("test.key.foo", "test.value.bar")
        val key = LuaValue.valueOf("test.key.foo")
        val value = LuaValue.valueOf("test.value.bar")
        val jse_value = jse_lib.get("getenv").call(key)
        //LuaValue jme_value = jme_lib.get("getenv").call(key);
        TestCase.assertEquals(value, jse_value)
        //assertEquals(value, jme_value);
    }

    companion object {

        internal val DAY = 24.0 * 3600.0
    }
}
