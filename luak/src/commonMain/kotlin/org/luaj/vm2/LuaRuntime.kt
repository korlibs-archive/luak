package org.luaj.vm2

class LuaRuntime {
    /** Simple cache of recently created strings that are short.
     * This is simply a list of strings, indexed by their hash codes modulo the cache size
     * that have been recently constructed.  If a string is being constructed frequently
     * from different contexts, it will generally show up as a cache hit and resolve
     * to the same value.   */
    val recent_short_strings = arrayOfNulls<LuaString>(LuaString.RECENT_STRINGS_CACHE_SIZE)
}
