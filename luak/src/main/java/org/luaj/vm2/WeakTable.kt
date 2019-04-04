/*******************************************************************************
 * Copyright (c) 2009-2011, 2013 Luaj.org. All rights reserved.
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

import org.luaj.vm2.LuaTable.Slot
import org.luaj.vm2.LuaTable.StrongSlot

/**
 * Subclass of [LuaTable] that provides weak key and weak value semantics.
 *
 *
 * Normally these are not created directly, but indirectly when changing the mode
 * of a [LuaTable] as lua script executes.
 *
 *
 * However, calling the constructors directly when weak tables are required from
 * Java will reduce overhead.
 */
class WeakTable
/**
 * Construct a table with weak keys, weak values, or both
 * @param weakkeys true to let the table have weak keys
 * @param weakvalues true to let the table have weak values
 */
    (private val weakkeys: Boolean, private val weakvalues: Boolean, private val backing: LuaValue) : Metatable {

    override fun useWeakKeys(): Boolean {
        return weakkeys
    }

    override fun useWeakValues(): Boolean {
        return weakvalues
    }

    override fun toLuaValue(): LuaValue {
        return backing
    }

    override fun entry(key: LuaValue, value: LuaValue): Slot? {
        var value: LuaValue? = value
        value = value?.strongvalue()
        if (value == null)
            return null
        if (weakkeys && !(key.isnumber() || key.isstring() || key.isboolean())) {
            return if (weakvalues && !(value.isnumber() || value.isstring() || value.isboolean())) {
                WeakKeyAndValueSlot(key, value, null)
            } else {
                WeakKeySlot(key, value, null)
            }
        }
        return if (weakvalues && !(value.isnumber() || value.isstring() || value.isboolean())) {
            WeakValueSlot(key, value, null)
        } else LuaTable.defaultEntry(key, value)
    }

    abstract class WeakSlot protected constructor(
        protected var key: Any?,
        protected var value: Any?,
        protected var next: Slot?
    ) : Slot {

        abstract override fun keyindex(hashMask: Int): Int

        abstract fun set(value: LuaValue): Slot

        override fun first(): StrongSlot? {
            val key = strongkey()
            val value = strongvalue()
            if (key != null && value != null) {
                return LuaTable.NormalEntry(key, value)
            } else {
                this.key = null
                this.value = null
                return null
            }
        }

        override fun find(key: LuaValue): StrongSlot? {
            val first = first()
            return first?.find(key)
        }

        override fun keyeq(key: LuaValue): Boolean {
            val first = first()
            return first != null && first.keyeq(key)
        }

        override fun rest(): Slot? {
            return next
        }

        override fun arraykey(max: Int): Int {
            // Integer keys can never be weak.
            return 0
        }

        override fun set(target: StrongSlot, value: LuaValue): Slot {
            val key = strongkey()
            if (key != null && target.find(key) != null) {
                return set(value)
            } else if (key != null) {
                // Our key is still good.
                next = next!!.set(target, value)
                return this
            } else {
                // our key was dropped, remove ourselves from the chain.
                return next!!.set(target, value)
            }
        }

        override fun add(entry: Slot): Slot? {
            next = if (next != null) next!!.add(entry) else entry
            return if (strongkey() != null && strongvalue() != null) {
                this
            } else {
                next
            }
        }

        override fun remove(target: StrongSlot): Slot {
            val key = strongkey()
            if (key == null) {
                return next!!.remove(target)
            } else if (target.keyeq(key)) {
                this.value = null
                return this
            } else {
                next = next!!.remove(target)
                return this
            }
        }

        override fun relink(rest: Slot?): Slot? {
            return if (strongkey() != null && strongvalue() != null) {
                if (rest == null && this.next == null) {
                    this
                } else {
                    copy(rest)
                }
            } else {
                rest
            }
        }

        open fun strongkey(): LuaValue? {
            return key as LuaValue?
        }

        open fun strongvalue(): LuaValue? {
            return value as LuaValue?
        }

        protected abstract fun copy(next: Slot?): WeakSlot
    }

    class WeakKeySlot : WeakSlot {

        private val keyhash: Int

        constructor(key: LuaValue, value: LuaValue, next: Slot?) : super(weaken(key), value, next) {
            keyhash = key.hashCode()
        }

        protected constructor(copyFrom: WeakKeySlot, next: Slot?) : super(copyFrom.key, copyFrom.value, next) {
            this.keyhash = copyFrom.keyhash
        }

        override fun keyindex(mask: Int): Int {
            return LuaTable.hashmod(keyhash, mask)
        }

        override fun set(value: LuaValue): Slot {
            this.value = value
            return this
        }

        override fun strongkey(): LuaValue? {
            return strengthen(key)
        }

        override fun copy(rest: Slot?): WeakSlot {
            return WeakKeySlot(this, rest)
        }
    }

    internal class WeakValueSlot : WeakSlot {

        constructor(key: LuaValue, value: LuaValue, next: Slot?) : super(key, weaken(value), next) {}

        protected constructor(copyFrom: WeakValueSlot, next: Slot?) : super(copyFrom.key, copyFrom.value, next) {}

        override fun keyindex(mask: Int): Int {
            return LuaTable.hashSlot(strongkey()!!, mask)
        }

        override fun set(value: LuaValue): Slot {
            this.value = weaken(value)
            return this
        }

        override fun strongvalue(): LuaValue? {
            return strengthen(value)
        }

        override fun copy(next: Slot?): WeakSlot {
            return WeakValueSlot(this, next)
        }
    }

    internal class WeakKeyAndValueSlot : WeakSlot {

        private val keyhash: Int

        constructor(key: LuaValue, value: LuaValue, next: Slot?) : super(weaken(key), weaken(value), next) {
            keyhash = key.hashCode()
        }

        protected constructor(copyFrom: WeakKeyAndValueSlot, next: Slot?) : super(copyFrom.key, copyFrom.value, next) {
            keyhash = copyFrom.keyhash
        }

        override fun keyindex(hashMask: Int): Int {
            return LuaTable.hashmod(keyhash, hashMask)
        }

        override fun set(value: LuaValue): Slot {
            this.value = weaken(value)
            return this
        }

        override fun strongkey(): LuaValue? {
            return strengthen(key)
        }

        override fun strongvalue(): LuaValue? {
            return strengthen(value)
        }

        override fun copy(next: Slot?): WeakSlot {
            return WeakKeyAndValueSlot(this, next)
        }
    }

    /** Internal class to implement weak values.
     * @see WeakTable
     */
    open class WeakValue(value: LuaValue) : LuaValue() {
        var ref: WeakReference<*>

        init {
            ref = WeakReference(value)
        }

        override fun type(): Int {
            illegal("type", "weak value")
        }

        override fun typename(): String {
            illegal("typename", "weak value")
        }

        override fun toString(): String {
            return "weak<" + ref.get() + ">"
        }

        override fun strongvalue(): LuaValue? {
            val o = ref.get()
            return o as LuaValue?
        }

        override fun raweq(rhs: LuaValue): Boolean {
            val o = ref.get()
            return o != null && rhs.raweq((o as LuaValue?)!!)
        }
    }

    /** Internal class to implement weak userdata values.
     * @see WeakTable
     */
    class WeakUserdata(value: LuaValue) : WeakValue(value) {
        private val ob: WeakReference<*>
        private val mt: LuaValue?

        init {
            ob = WeakReference(value.touserdata())
            mt = value.getmetatable()
        }

        override fun strongvalue(): LuaValue? {
            val u = ref.get()
            if (u != null)
                return u as LuaValue?
            val o = ob.get()
            if (o != null) {
                val ud = LuaValue.userdataOf(o, mt)
                ref = WeakReference(ud)
                return ud
            } else {
                return null
            }
        }
    }

    override fun wrap(value: LuaValue): LuaValue {
        return if (weakvalues) weaken(value) else value
    }

    override fun arrayget(array: Array<LuaValue?>, index: Int): LuaValue? {
        var value: LuaValue? = array[index]
        if (value != null) {
            value = strengthen(value)
            if (value == null) {
                array[index] = null
            }
        }
        return value
    }

    companion object {

        fun make(weakkeys: Boolean, weakvalues: Boolean): LuaTable {
            val mode: LuaString
            if (weakkeys && weakvalues) {
                mode = LuaString.valueOf("kv")
            } else if (weakkeys) {
                mode = LuaString.valueOf("k")
            } else if (weakvalues) {
                mode = LuaString.valueOf("v")
            } else {
                return LuaValue.tableOf()
            }
            val table = LuaValue.tableOf()
            val mt = LuaValue.tableOf(arrayOf(LuaValue.MODE, mode))
            table.setmetatable(mt)
            return table
        }

        /**
         * Self-sent message to convert a value to its weak counterpart
         * @param value value to convert
         * @return [LuaValue] that is a strong or weak reference, depending on type of `value`
         */
        protected fun weaken(value: LuaValue): LuaValue {
            when (value.type()) {
                LuaValue.TFUNCTION, LuaValue.TTHREAD, LuaValue.TTABLE -> return WeakValue(value)
                LuaValue.TUSERDATA -> return WeakUserdata(value)
                else -> return value
            }
        }

        /**
         * Unwrap a LuaValue from a WeakReference and/or WeakUserdata.
         * @param ref reference to convert
         * @return LuaValue or null
         * @see .weaken
         */
        protected fun strengthen(ref: Any?): LuaValue? {
            var ref = ref
            if (ref is WeakReference<*>) {
                ref = ref.get()
            }
            return if (ref is WeakValue) {
                ref.strongvalue()
            } else ref as LuaValue?
        }
    }
}
