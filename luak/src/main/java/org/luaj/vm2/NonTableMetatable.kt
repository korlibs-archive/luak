package org.luaj.vm2

import org.luaj.vm2.LuaTable.Slot

internal class NonTableMetatable(private val value: LuaValue) : Metatable {

    override fun useWeakKeys(): Boolean {
        return false
    }

    override fun useWeakValues(): Boolean {
        return false
    }

    override fun toLuaValue(): LuaValue {
        return value
    }

    override fun entry(key: LuaValue, value: LuaValue): Slot {
        return LuaTable.defaultEntry(key, value)
    }

    override fun wrap(value: LuaValue): LuaValue {
        return value
    }

    override fun arrayget(array: Array<LuaValue?>, index: Int): LuaValue? {
        return array[index]
    }
}
