package org.luaj.vm2

import kotlin.reflect.*

fun LuaValue.isuserdata(c: Class<*>): Boolean = isuserdata(c.kotlin)
fun LuaValue.optuserdata(c: Class<*>, defval: Any?): Any? = optuserdata(c.kotlin, defval)
fun LuaValue.checkuserdata(c: Class<*>): Any? = checkuserdata(c.kotlin)
