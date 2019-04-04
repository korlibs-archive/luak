/*******************************************************************************
 * Copyright (c) 2007-2012 LuaJ. All rights reserved.
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


import java.lang.ref.WeakReference

/**
 * Subclass of [LuaValue] that implements
 * a lua coroutine thread using Java Threads.
 *
 *
 * A LuaThread is typically created in response to a scripted call to
 * `coroutine.create()`
 *
 *
 * The threads must be initialized with the globals, so that
 * the global environment may be passed along according to rules of lua.
 * This is done via the constructor arguments [.LuaThread] or
 * [.LuaThread].
 *
 *
 * The utility classes [org.luaj.vm2.lib.jse.JsePlatform] and
 * [org.luaj.vm2.lib.jme.JmePlatform]
 * see to it that this [Globals] are initialized properly.
 *
 *
 * The behavior of coroutine threads matches closely the behavior
 * of C coroutine library.  However, because of the use of Java threads
 * to manage call state, it is possible to yield from anywhere in luaj.
 *
 *
 * Each Java thread wakes up at regular intervals and checks a weak reference
 * to determine if it can ever be resumed.  If not, it throws
 * [OrphanedThread] which is an [Error].
 * Applications should not catch [OrphanedThread], because it can break
 * the thread safety of luaj.  The value controlling the polling interval
 * is [.thread_orphan_check_interval] and may be set by the user.
 *
 *
 * There are two main ways to abandon a coroutine.  The first is to call
 * `yield()` from lua, or equivalently [Globals.yield],
 * and arrange to have it never resumed possibly by values passed to yield.
 * The second is to throw [OrphanedThread], which should put the thread
 * in a dead state.   In either case all references to the thread must be
 * dropped, and the garbage collector must run for the thread to be
 * garbage collected.
 *
 *
 * @see LuaValue
 *
 * @see org.luaj.vm2.lib.jse.JsePlatform
 *
 * @see org.luaj.vm2.lib.jme.JmePlatform
 *
 * @see org.luaj.vm2.lib.CoroutineLib
 */
class LuaThread : LuaValue {

    @JvmField
    val state: State

    /** Thread-local used by DebugLib to store debugging state.
     * This is an opaque value that should not be modified by applications.  */
    @JvmField
    var callstack: Any? = null

    @JvmField
    val globals: Globals

    /** Error message handler for this thread, if any.   */
    @JvmField
    var errorfunc: LuaValue? = null

    val status: String
        get() = STATUS_NAMES[state.status]

    val isMainThread: Boolean
        get() = this.state.function == null

    /** Private constructor for main thread only  */
    constructor(globals: Globals) {
        state = State(globals, this, null)
        state.status = STATUS_RUNNING
        this.globals = globals
    }

    /**
     * Create a LuaThread around a function and environment
     * @param func The function to execute
     */
    constructor(globals: Globals, func: LuaValue?) {
        LuaValue.assert_(func != null, "function cannot be null")
        state = State(globals, this, func)
        this.globals = globals
    }

    override fun type(): Int {
        return LuaValue.TTHREAD
    }

    override fun typename(): String {
        return "thread"
    }

    override fun isthread(): Boolean {
        return true
    }

    override fun optthread(defval: LuaThread?): LuaThread? {
        return this
    }

    override fun checkthread(): LuaThread? {
        return this
    }

    override fun getmetatable(): LuaValue? {
        return s_metatable
    }

    fun resume(args: Varargs): Varargs {
        val s = this.state
        return if (s.status > LuaThread.STATUS_SUSPENDED) LuaValue.varargsOf(
            LuaValue.FALSE,
            LuaValue.valueOf("cannot resume " + (if (s.status == LuaThread.STATUS_DEAD) "dead" else "non-suspended") + " coroutine")
        ) else s.lua_resume(this, args)
    }

    class State internal constructor(private val globals: Globals, lua_thread: LuaThread, val function: LuaValue?) :
        Runnable {
        @JvmField
        internal val lua_thread: WeakReference<*> = WeakReference(lua_thread)
        @JvmField
        internal var args: Varargs = LuaValue.NONE
        @JvmField
        internal var result: Varargs = LuaValue.NONE
        @JvmField
        internal var error: String? = null

        /** Hook function control state used by debug lib.  */
        @JvmField
        var hookfunc: LuaValue? = null

        @JvmField
        var hookline: Boolean = false
        @JvmField
        var hookcall: Boolean = false
        @JvmField
        var hookrtrn: Boolean = false
        @JvmField
        var hookcount: Int = 0
        @JvmField
        var inhook: Boolean = false
        @JvmField
        var lastline: Int = 0
        @JvmField
        var bytecodes: Int = 0

        @JvmField
        var status = LuaThread.STATUS_INITIAL

        @Synchronized
        override fun run() {
            try {
                val a = this.args
                this.args = LuaValue.NONE
                this.result = function!!.invoke(a)
            } catch (t: Throwable) {
                this.error = t.message
            } finally {
                this.status = LuaThread.STATUS_DEAD
                (this as java.lang.Object).notify()
            }
        }

        @Synchronized
        fun lua_resume(new_thread: LuaThread, args: Varargs): Varargs {
            val previous_thread = globals.running
            try {
                globals.running = new_thread
                this.args = args
                if (this.status == STATUS_INITIAL) {
                    this.status = STATUS_RUNNING
                    Thread(this, "Coroutine-" + ++coroutine_count).start()
                } else {
                    (this as java.lang.Object).notify()
                }
                if (previous_thread != null)
                    previous_thread.state.status = STATUS_NORMAL
                this.status = STATUS_RUNNING
                (this as java.lang.Object).wait()
                return if (this.error != null)
                    LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf(this.error))
                else
                    LuaValue.varargsOf(LuaValue.TRUE, this.result)
            } catch (ie: InterruptedException) {
                throw OrphanedThread()
            } finally {
                this.args = LuaValue.NONE
                this.result = LuaValue.NONE
                this.error = null
                globals.running = previous_thread
                if (previous_thread != null) {
                    previous_thread.state.status = STATUS_RUNNING
                }
            }
        }

        @Synchronized
        fun lua_yield(args: Varargs): Varargs {
            try {
                this.result = args
                this.status = STATUS_SUSPENDED
                (this as java.lang.Object).notify()
                do {
                    (this as java.lang.Object).wait(thread_orphan_check_interval)
                    if (this.lua_thread.get() == null) {
                        this.status = STATUS_DEAD
                        throw OrphanedThread()
                    }
                } while (this.status == STATUS_SUSPENDED)
                return this.args
            } catch (ie: InterruptedException) {
                this.status = STATUS_DEAD
                throw OrphanedThread()
            } finally {
                this.args = LuaValue.NONE
                this.result = LuaValue.NONE
            }
        }
    }

    companion object {

        /** Shared metatable for lua threads.  */
        @JvmField
        var s_metatable: LuaValue? = null

        /** The current number of coroutines.  Should not be set.  */
        @JvmField
        var coroutine_count = 0

        /** Polling interval, in milliseconds, which each thread uses while waiting to
         * return from a yielded state to check if the lua threads is no longer
         * referenced and therefore should be garbage collected.
         * A short polling interval for many threads will consume server resources.
         * Orphaned threads cannot be detected and collected unless garbage
         * collection is run.  This can be changed by Java startup code if desired.
         */
        @JvmField
        var thread_orphan_check_interval: Long = 5000

        @JvmField
        val STATUS_INITIAL = 0
        @JvmField
        val STATUS_SUSPENDED = 1
        @JvmField
        val STATUS_RUNNING = 2
        @JvmField
        val STATUS_NORMAL = 3
        @JvmField
        val STATUS_DEAD = 4
        @JvmField
        val STATUS_NAMES = arrayOf("suspended", "suspended", "running", "normal", "dead")

        @JvmField
        val MAX_CALLSTACK = 256
    }

}
