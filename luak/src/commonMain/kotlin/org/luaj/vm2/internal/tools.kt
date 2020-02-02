package org.luaj.vm2.internal

// @TODO: Make this internal
internal fun arraycopy(src: ByteArray, srcPos: Int, dst: ByteArray, dstPos: Int, count: Int) = src.copyInto(dst, dstPos, srcPos, srcPos + count)
internal fun arraycopy(src: CharArray, srcPos: Int, dst: CharArray, dstPos: Int, count: Int) = src.copyInto(dst, dstPos, srcPos, srcPos + count)
internal fun arraycopy(src: IntArray, srcPos: Int, dst: IntArray, dstPos: Int, count: Int) = src.copyInto(dst, dstPos, srcPos, srcPos + count)
@Suppress("UNCHECKED_CAST")
internal fun arraycopy(src: Array<*>, srcPos: Int, dst: Array<*>, dstPos: Int, count: Int) = (src as Array<Any>).copyInto(dst as Array<Any>, dstPos, srcPos, srcPos + count)
