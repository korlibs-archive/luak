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
package org.luaj.vm2.lib

import java.io.IOException
import java.util.Calendar
import java.util.Date

import org.luaj.vm2.Buffer
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

/**
 * Subclass of [LibFunction] which implements the standard lua `os` library.
 *
 *
 * It is a usable base with simplified stub functions
 * for library functions that cannot be implemented uniformly
 * on Jse and Jme.
 *
 *
 * This can be installed as-is on either platform, or extended
 * and refined to be used in a complete Jse implementation.
 *
 *
 * Because the nature of the `os` library is to encapsulate
 * os-specific features, the behavior of these functions varies considerably
 * from their counterparts in the C platform.
 *
 *
 * The following functions have limited implementations of features
 * that are not supported well on Jme:
 *
 *  * `execute()`
 *  * `remove()`
 *  * `rename()`
 *  * `tmpname()`
 *
 *
 *
 * Typically, this library is included as part of a call to either
 * [org.luaj.vm2.lib.jse.JsePlatform.standardGlobals] or [org.luaj.vm2.lib.jme.JmePlatform.standardGlobals]
 * <pre> `Globals globals = JsePlatform.standardGlobals();
 * System.out.println( globals.get("os").get("time").call() );
` *  </pre>
 * In this example the platform-specific [org.luaj.vm2.lib.jse.JseOsLib] library will be loaded, which will include
 * the base functionality provided by this class.
 *
 *
 * To instantiate and use it directly,
 * link it into your globals table via [LuaValue.load] using code such as:
 * <pre> `Globals globals = new Globals();
 * globals.load(new JseBaseLib());
 * globals.load(new PackageLib());
 * globals.load(new OsLib());
 * System.out.println( globals.get("os").get("time").call() );
` *  </pre>
 *
 *
 * @see LibFunction
 *
 * @see org.luaj.vm2.lib.jse.JseOsLib
 *
 * @see org.luaj.vm2.lib.jse.JsePlatform
 *
 * @see org.luaj.vm2.lib.jme.JmePlatform
 *
 * @see [http://www.lua.org/manual/5.1/manual.html.5.8](http://www.lua.org/manual/5.1/manual.html.5.8)
 */
/**
 * Create and OsLib instance.
 */
open class OsLib : TwoArgFunction() {

    @JvmField protected var globals: Globals? = null

    /** Perform one-time initialization on the library by creating a table
     * containing the library functions, adding that table to the supplied environment,
     * adding the table to package.loaded, and returning table as the return value.
     * @param modname the module name supplied if this is loaded via 'require'.
     * @param env the environment to load into, typically a Globals instance.
     */
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        globals = env.checkglobals()
        val os = LuaTable()
        for (i in NAMES.indices)
            os[NAMES[i]] = OsLibFunc(i, NAMES[i])
        env["os"] = os
        env["package"]["loaded"]["os"] = os
        return os
    }

    internal inner class OsLibFunc(opcode: Int, name: String) : VarArgFunction() {
        init {
            this.opcode = opcode
            this.name = name
        }

        override fun invoke(args: Varargs): Varargs {
            try {
                when (opcode) {
                    CLOCK -> return LuaValue.valueOf(clock())
                    DATE -> {
                        val s = args.optjstring(1, "%c")
                        val t = if (args.isnumber(2)) args.todouble(2) else time(null)
                        if (s == "*t") {
                            val d = Calendar.getInstance()
                            d.time = Date((t * 1000).toLong())
                            val tbl = LuaValue.tableOf()
                            tbl["year"] = LuaValue.valueOf(d.get(Calendar.YEAR))
                            tbl["month"] = LuaValue.valueOf(d.get(Calendar.MONTH) + 1)
                            tbl["day"] = LuaValue.valueOf(d.get(Calendar.DAY_OF_MONTH))
                            tbl["hour"] = LuaValue.valueOf(d.get(Calendar.HOUR_OF_DAY))
                            tbl["min"] = LuaValue.valueOf(d.get(Calendar.MINUTE))
                            tbl["sec"] = LuaValue.valueOf(d.get(Calendar.SECOND))
                            tbl["wday"] = LuaValue.valueOf(d.get(Calendar.DAY_OF_WEEK))
                            tbl["yday"] = LuaValue.valueOf(d.get(0x6)) // Day of year
                            tbl["isdst"] = LuaValue.valueOf(isDaylightSavingsTime(d))
                            return tbl
                        }
                        return LuaValue.valueOf(date(s!!, if (t == -1.0) time(null) else t))
                    }
                    DIFFTIME -> return LuaValue.valueOf(difftime(args.checkdouble(1), args.checkdouble(2)))
                    EXECUTE -> return execute(args.optjstring(1, null!!))
                    EXIT -> {
                        exit(args.optint(1, 0))
                        return LuaValue.NONE
                    }
                    GETENV -> {
                        val `val` = getenv(args.checkjstring(1))
                        return if (`val` != null) LuaValue.valueOf(`val`) else LuaValue.NIL
                    }
                    REMOVE -> {
                        remove(args.checkjstring(1))
                        return LuaValue.TRUE
                    }
                    RENAME -> {
                        rename(args.checkjstring(1), args.checkjstring(2))
                        return LuaValue.TRUE
                    }
                    SETLOCALE -> {
                        val s = setlocale(args.optjstring(1, null!!), args.optjstring(2, "all"))
                        return if (s != null) LuaValue.valueOf(s) else LuaValue.NIL
                    }
                    TIME -> return LuaValue.valueOf(time(args.opttable(1, null!!)))
                    TMPNAME -> return LuaValue.valueOf(tmpname())
                }
                return LuaValue.NONE
            } catch (e: IOException) {
                return LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message!!))
            }

        }
    }

    /**
     * @return an approximation of the amount in seconds of CPU time used by
     * the program.  For luaj this simple returns the elapsed time since the
     * OsLib class was loaded.
     */
    protected fun clock(): Double {
        return (System.currentTimeMillis() - t0) / 1000.0
    }

    /**
     * Returns the number of seconds from time t1 to time t2.
     * In POSIX, Windows, and some other systems, this value is exactly t2-t1.
     * @param t2
     * @param t1
     * @return diffeence in time values, in seconds
     */
    protected fun difftime(t2: Double, t1: Double): Double {
        return t2 - t1
    }

    /**
     * If the time argument is present, this is the time to be formatted
     * (see the os.time function for a description of this value).
     * Otherwise, date formats the current time.
     *
     * Date returns the date as a string,
     * formatted according to the same rules as ANSII strftime, but without
     * support for %g, %G, or %V.
     *
     * When called without arguments, date returns a reasonable date and
     * time representation that depends on the host system and on the
     * current locale (that is, os.date() is equivalent to os.date("%c")).
     *
     * @param format
     * @param time time since epoch, or -1 if not supplied
     * @return a LString or a LTable containing date and time,
     * formatted according to the given string format.
     */
    fun date(format: String, time: Double): String {
        var format = format
        var time = time
        val d = Calendar.getInstance()
        d.time = Date((time * 1000).toLong())
        if (format.startsWith("!")) {
            time -= timeZoneOffset(d).toDouble()
            d.time = Date((time * 1000).toLong())
            format = format.substring(1)
        }
        val fmt = format.toByteArray()
        val n = fmt.size
        val result = Buffer(n)
        var c: Byte
        var i = 0
        loop@while (i < n) {
            when (run { c = fmt[i++]; c }.toChar()) {
                '\n' -> result.append("\n")
                '%' -> {
                    if (i >= n) break@loop
                    when (run { c = fmt[i++]; c }.toChar()) {
                        '%' -> result.append('%'.toByte())
                        'a' -> result.append(WeekdayNameAbbrev[d.get(Calendar.DAY_OF_WEEK) - 1])
                        'A' -> result.append(WeekdayName[d.get(Calendar.DAY_OF_WEEK) - 1])
                        'b' -> result.append(MonthNameAbbrev[d.get(Calendar.MONTH)])
                        'B' -> result.append(MonthName[d.get(Calendar.MONTH)])
                        'c' -> result.append(date("%a %b %d %H:%M:%S %Y", time))
                        'd' -> result.append((100 + d.get(Calendar.DAY_OF_MONTH)).toString().substring(1))
                        'H' -> result.append((100 + d.get(Calendar.HOUR_OF_DAY)).toString().substring(1))
                        'I' -> result.append((100 + d.get(Calendar.HOUR_OF_DAY) % 12).toString().substring(1))
                        'j' -> { // day of year.
                            val y0 = beginningOfYear(d)
                            val dayOfYear = ((d.time.time - y0.time.time) / (24 * 3600L * 1000L)).toInt()
                            result.append((1001 + dayOfYear).toString().substring(1))
                        }
                        'm' -> result.append((101 + d.get(Calendar.MONTH)).toString().substring(1))
                        'M' -> result.append((100 + d.get(Calendar.MINUTE)).toString().substring(1))
                        'p' -> result.append(if (d.get(Calendar.HOUR_OF_DAY) < 12) "AM" else "PM")
                        'S' -> result.append((100 + d.get(Calendar.SECOND)).toString().substring(1))
                        'U' -> result.append(weekNumber(d, 0).toString())
                        'w' -> result.append(((d.get(Calendar.DAY_OF_WEEK) + 6) % 7).toString())
                        'W' -> result.append(weekNumber(d, 1).toString())
                        'x' -> result.append(date("%m/%d/%y", time))
                        'X' -> result.append(date("%H:%M:%S", time))
                        'y' -> result.append(d.get(Calendar.YEAR).toString().substring(2))
                        'Y' -> result.append(d.get(Calendar.YEAR).toString())
                        'z' -> {
                            val tzo = timeZoneOffset(d) / 60
                            val a = Math.abs(tzo)
                            val h = (100 + a / 60).toString().substring(1)
                            val m = (100 + a % 60).toString().substring(1)
                            result.append((if (tzo >= 0) "+" else "-") + h + m)
                        }
                        else -> LuaValue.argerror(1, "invalid conversion specifier '%$c'")
                    }
                }
                else -> result.append(c)
            }
        }
        return result.tojstring()
    }

    private fun beginningOfYear(d: Calendar): Calendar {
        val y0 = Calendar.getInstance()
        y0.time = d.time
        y0.set(Calendar.MONTH, 0)
        y0.set(Calendar.DAY_OF_MONTH, 1)
        y0.set(Calendar.HOUR_OF_DAY, 0)
        y0.set(Calendar.MINUTE, 0)
        y0.set(Calendar.SECOND, 0)
        y0.set(Calendar.MILLISECOND, 0)
        return y0
    }

    private fun weekNumber(d: Calendar, startDay: Int): Int {
        val y0 = beginningOfYear(d)
        y0.set(Calendar.DAY_OF_MONTH, 1 + (startDay + 8 - y0.get(Calendar.DAY_OF_WEEK)) % 7)
        if (y0.after(d)) {
            y0.set(Calendar.YEAR, y0.get(Calendar.YEAR) - 1)
            y0.set(Calendar.DAY_OF_MONTH, 1 + (startDay + 8 - y0.get(Calendar.DAY_OF_WEEK)) % 7)
        }
        val dt = d.time.time - y0.time.time
        return 1 + (dt / (7L * 24L * 3600L * 1000L)).toInt()
    }

    private fun timeZoneOffset(d: Calendar): Int {
        val localStandarTimeMillis = (d.get(Calendar.HOUR_OF_DAY) * 3600 +
                d.get(Calendar.MINUTE) * 60 +
                d.get(Calendar.SECOND)) * 1000
        return d.timeZone.getOffset(
            1,
            d.get(Calendar.YEAR),
            d.get(Calendar.MONTH),
            d.get(Calendar.DAY_OF_MONTH),
            d.get(Calendar.DAY_OF_WEEK),
            localStandarTimeMillis
        ) / 1000
    }

    private fun isDaylightSavingsTime(d: Calendar): Boolean {
        return timeZoneOffset(d) != d.timeZone.rawOffset / 1000
    }

    /**
     * This function is equivalent to the C function system.
     * It passes command to be executed by an operating system shell.
     * It returns a status code, which is system-dependent.
     * If command is absent, then it returns nonzero if a shell
     * is available and zero otherwise.
     * @param command command to pass to the system
     */
    protected open fun execute(command: String?): Varargs {
        return LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf("exit"), LuaValue.ONE)
    }

    /**
     * Calls the C function exit, with an optional code, to terminate the host program.
     * @param code
     */
    protected fun exit(code: Int) {
        System.exit(code)
    }

    /**
     * Returns the value of the process environment variable varname,
     * or the System property value for varname,
     * or null if the variable is not defined in either environment.
     *
     * The default implementation, which is used by the JmePlatform,
     * only queryies System.getProperty().
     *
     * The JsePlatform overrides this behavior and returns the
     * environment variable value using System.getenv() if it exists,
     * or the System property value if it does not.
     *
     * A SecurityException may be thrown if access is not allowed
     * for 'varname'.
     * @param varname
     * @return String value, or null if not defined
     */
    protected open fun getenv(varname: String?): String? {
        return System.getProperty(varname!!)
    }

    /**
     * Deletes the file or directory with the given name.
     * Directories must be empty to be removed.
     * If this function fails, it throws and IOException
     *
     * @param filename
     * @throws IOException if it fails
     */

    protected open fun remove(filename: String?) {
        throw IOException("not implemented")
    }

    /**
     * Renames file or directory named oldname to newname.
     * If this function fails,it throws and IOException
     *
     * @param oldname old file name
     * @param newname new file name
     * @throws IOException if it fails
     */

    protected open fun rename(oldname: String?, newname: String?) {
        throw IOException("not implemented")
    }

    /**
     * Sets the current locale of the program. locale is a string specifying
     * a locale; category is an optional string describing which category to change:
     * "all", "collate", "ctype", "monetary", "numeric", or "time"; the default category
     * is "all".
     *
     * If locale is the empty string, the current locale is set to an implementation-
     * defined native locale. If locale is the string "C", the current locale is set
     * to the standard C locale.
     *
     * When called with null as the first argument, this function only returns the
     * name of the current locale for the given category.
     *
     * @param locale
     * @param category
     * @return the name of the new locale, or null if the request
     * cannot be honored.
     */
    protected fun setlocale(locale: String?, category: String?): String {
        return "C"
    }

    /**
     * Returns the current time when called without arguments,
     * or a time representing the date and time specified by the given table.
     * This table must have fields year, month, and day,
     * and may have fields hour, min, sec, and isdst
     * (for a description of these fields, see the os.date function).
     * @param table
     * @return long value for the time
     */
    protected fun time(table: LuaTable?): Double {
        val d: Date
        if (table == null) {
            d = Date()
        } else {
            val c = Calendar.getInstance()
            c.set(Calendar.YEAR, table["year"].checkint())
            c.set(Calendar.MONTH, table["month"].checkint() - 1)
            c.set(Calendar.DAY_OF_MONTH, table["day"].checkint())
            c.set(Calendar.HOUR_OF_DAY, table["hour"].optint(12))
            c.set(Calendar.MINUTE, table["min"].optint(0))
            c.set(Calendar.SECOND, table["sec"].optint(0))
            c.set(Calendar.MILLISECOND, 0)
            d = c.time
        }
        return d.time / 1000.0
    }

    /**
     * Returns a string with a file name that can be used for a temporary file.
     * The file must be explicitly opened before its use and explicitly removed
     * when no longer needed.
     *
     * On some systems (POSIX), this function also creates a file with that name,
     * to avoid security risks. (Someone else might create the file with wrong
     * permissions in the time between getting the name and creating the file.)
     * You still have to open the file to use it and to remove it (even if you
     * do not use it).
     *
     * @return String filename to use
     */
    protected open fun tmpname(): String {
        synchronized(OsLib::class.java) {
            return TMP_PREFIX + tmpnames++ + TMP_SUFFIX
        }
    }

    companion object {
        @JvmField var TMP_PREFIX = ".luaj"
        @JvmField var TMP_SUFFIX = "tmp"

        private const val CLOCK = 0
        private const val DATE = 1
        private const val DIFFTIME = 2
        private const val EXECUTE = 3
        private const val EXIT = 4
        private const val GETENV = 5
        private const val REMOVE = 6
        private const val RENAME = 7
        private const val SETLOCALE = 8
        private const val TIME = 9
        private const val TMPNAME = 10

        private val NAMES = arrayOf(
            "clock",
            "date",
            "difftime",
            "execute",
            "exit",
            "getenv",
            "remove",
            "rename",
            "setlocale",
            "time",
            "tmpname"
        )

        private val t0 = System.currentTimeMillis()
        private var tmpnames = t0

        private val WeekdayNameAbbrev = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        private val WeekdayName = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        private val MonthNameAbbrev =
            arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        private val MonthName = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
    }
}
