/*******************************************************************************
 * Copyright (c) 2008-2013 LuaJ. All rights reserved.
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
package org.luaj.vm2.script

import java.io.*

import javax.script.*

import org.luaj.vm2.*
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua

/**
 * Implementation of the ScriptEngine interface which can compile and execute
 * scripts using luaj.
 *
 *
 *
 * This engine requires the types of the Bindings and ScriptContext to be
 * compatible with the engine.  For creating new client context use
 * ScriptEngine.createContext() which will return [LuajContext],
 * and for client bindings use the default engine scoped bindings or
 * construct a [LuajBindings] directly.
 */
class LuaScriptEngine : AbstractScriptEngine(), ScriptEngine, Compilable {
    val ctx = LuajContext().also {
        it.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE)
        setContext(it)
    }

    init {
        // set special values
        put(ScriptEngine.LANGUAGE_VERSION, __LANGUAGE_VERSION__)
        put(ScriptEngine.LANGUAGE, __LANGUAGE__)
        put(ScriptEngine.ENGINE, __NAME__)
        put(ScriptEngine.ENGINE_VERSION, __ENGINE_VERSION__)
        put(ScriptEngine.ARGV, __ARGV__)
        put(ScriptEngine.FILENAME, __FILENAME__)
        put(ScriptEngine.NAME, __SHORT_NAME__)
        put("THREADING", null)
    }

    @Throws(ScriptException::class)
    override fun compile(script: String): CompiledScript {
        return compile(StringReader(script))
    }

    @Throws(ScriptException::class)
    override fun compile(script: Reader): CompiledScript {
        try {
            val `is` = Utf8Encoder(script)
            try {
                val g = ctx.globals
                val f = g.load(script, "script").checkfunction()
                return LuajCompiledScript(f!!, g)
            } catch (lee: LuaError) {
                throw ScriptException(lee.message)
            } finally {
                `is`.close()
            }
        } catch (e: Exception) {
            throw ScriptException("eval threw $e")
        }

    }

    @Throws(ScriptException::class)
    override fun eval(reader: Reader, bindings: Bindings): Any? {
        return (compile(reader) as LuajCompiledScript).eval(ctx.globals, bindings)
    }

    @Throws(ScriptException::class)
    override fun eval(script: String, bindings: Bindings): Any? {
        return eval(StringReader(script), bindings)
    }

    override fun getScriptContext(nn: Bindings): ScriptContext {
        throw IllegalStateException("LuajScriptEngine should not be allocating contexts.")
    }

    override fun createBindings(): Bindings {
        return SimpleBindings()
    }

    @Throws(ScriptException::class)
    override fun eval(script: String, context: ScriptContext): Any {
        return eval(StringReader(script), context)
    }

    @Throws(ScriptException::class)
    override fun eval(reader: Reader, context: ScriptContext): Any {
        return compile(reader).eval(context)
    }

    override fun getFactory(): ScriptEngineFactory {
        return myFactory
    }


    internal inner class LuajCompiledScript(val function: LuaFunction, val compiling_globals: Globals) :
        CompiledScript() {

        override fun getEngine(): ScriptEngine {
            return this@LuaScriptEngine
        }

        @Throws(ScriptException::class)
        override fun eval(): Any? {
            return eval(getContext())
        }

        @Throws(ScriptException::class)
        override fun eval(bindings: Bindings): Any? {
            return eval((getContext() as LuajContext).globals, bindings)
        }

        @Throws(ScriptException::class)
        override fun eval(context: ScriptContext): Any? {
            return eval((context as LuajContext).globals, context.getBindings(ScriptContext.ENGINE_SCOPE))
        }

        @Throws(ScriptException::class)
        fun eval(g: Globals, b: Bindings): Any? {
            g.setmetatable(BindingsMetatable(b))
            var f = function
            if (f.isclosure())
                f = LuaClosure(f.checkclosure()!!.p, g)
            else {
                try {
                    f = f.javaClass.newInstance()
                } catch (e: Exception) {
                    throw ScriptException(e)
                }

                f.initupvalue1(g)
            }
            return toJava(f.invoke(LuaValue.NONE))
        }
    }

    // ------ convert char stream to byte stream for lua compiler -----

    inner class Utf8Encoder constructor(private val r: Reader) : InputStream() {
        private val buf = IntArray(2)
        private var n: Int = 0

        @Throws(IOException::class)
        override fun read(): Int {
            if (n > 0)
                return buf[--n]
            val c = r.read()
            if (c < 0x80)
                return c
            n = 0
            if (c < 0x800) {
                buf[n++] = 0x80 or (c and 0x3f)
                return 0xC0 or (c shr 6 and 0x1f)
            } else {
                buf[n++] = 0x80 or (c and 0x3f)
                buf[n++] = 0x80 or (c shr 6 and 0x3f)
                return 0xE0 or (c shr 12 and 0x0f)
            }
        }
    }

    internal class BindingsMetatable(bindings: Bindings) : LuaTable() {

        init {
            this.rawset(LuaValue.INDEX, object : TwoArgFunction() {
                override fun call(table: LuaValue, key: LuaValue): LuaValue {
                    return if (key.isstring())
                        toLua(bindings[key.tojstring()])
                    else
                        this.rawget(key)
                }
            })
            this.rawset(LuaValue.NEWINDEX, object : ThreeArgFunction() {
                override fun call(table: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
                    if (key.isstring()) {
                        val k = key.tojstring()
                        val v = toJava(value)
                        if (v == null)
                            bindings.remove(k)
                        else
                            bindings[k] = v
                    } else {
                        this.rawset(key, value)
                    }
                    return LuaValue.NONE
                }
            })
        }
    }

    companion object {

        private val __ENGINE_VERSION__ = Lua._VERSION
        private val __NAME__ = "Luaj"
        private val __SHORT_NAME__ = "Luaj"
        private val __LANGUAGE__ = "lua"
        private val __LANGUAGE_VERSION__ = "5.2"
        private val __ARGV__ = "arg"
        private val __FILENAME__ = "?"

        private val myFactory = LuaScriptEngineFactory()

        private fun toLua(javaValue: Any?): LuaValue {
            return if (javaValue == null)
                LuaValue.NIL
            else javaValue as? LuaValue ?: CoerceJavaToLua.coerce(javaValue)
        }

        private fun toJava(luajValue: LuaValue): Any? {
            when (luajValue.type()) {
                LuaValue.TNIL -> return null
                LuaValue.TSTRING -> return luajValue.tojstring()
                LuaValue.TUSERDATA -> return luajValue.checkuserdata(Any::class.java)
                LuaValue.TNUMBER -> return if (luajValue.isinttype())
                    luajValue.toint()
                else
                    luajValue.todouble()
                else -> return luajValue
            }
        }

        private fun toJava(v: Varargs): Any? {
            val n = v.narg()
            when (n) {
                0 -> return null
                1 -> return toJava(v.arg1())
                else -> {
                    val o = arrayOfNulls<Any>(n)
                    for (i in 0 until n)
                        o[i] = toJava(v.arg(i + 1))
                    return o
                }
            }
        }
    }

}
