package org.luaj.vm2.compiler

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.PrintStream
import java.net.MalformedURLException
import java.net.URL

import junit.framework.TestCase

import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.Print
import org.luaj.vm2.Prototype
import org.luaj.vm2.lib.jse.JsePlatform

abstract class AbstractUnitTests(zipdir: String, zipfile: String, private val dir: String) : TestCase() {
    private val jar: String
    private var globals: Globals? = null

    init {
        var zip: URL? = null
        zip = javaClass.getResource(zipfile)
        if (zip == null) {
            val file = File("$zipdir/$zipfile")
            try {
                if (file.exists())
                    zip = file.toURI().toURL()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

        }
        if (zip == null)
            throw RuntimeException("not found: $zipfile")
        this.jar = "jar:" + zip.toExternalForm() + "!/"
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        globals = JsePlatform.standardGlobals()
    }

    protected fun pathOfFile(file: String): String {
        return "$jar$dir/$file"
    }

    @Throws(IOException::class)
    protected fun inputStreamOfPath(path: String): InputStream {
        val url = URL(path)
        return url.openStream()
    }

    @Throws(IOException::class)
    protected fun inputStreamOfFile(file: String): InputStream {
        return inputStreamOfPath(pathOfFile(file))
    }

    protected open fun doTest(file: String) {
        try {
            // load source from jar
            val path = pathOfFile(file)
            val lua = bytesFromJar(path)

            // compile in memory
            val `is` = ByteArrayInputStream(lua)
            val p = globals!!.loadPrototype(`is`, "@$file", "bt")
            val actual = protoToString(p)

            // load expected value from jar
            val luac = bytesFromJar(path.substring(0, path.length - 4) + ".lc")
            val e = loadFromBytes(luac, file)
            val expected = protoToString(e)

            // compare results
            TestCase.assertEquals(expected, actual)

            // dump into memory
            val baos = ByteArrayOutputStream()
            DumpState.dump(p, baos, false)
            val dumped = baos.toByteArray()

            // re-undump
            val p2 = loadFromBytes(dumped, file)
            val actual2 = protoToString(p2)

            // compare again
            TestCase.assertEquals(actual, actual2)

        } catch (e: IOException) {
            TestCase.fail(e.toString())
        }

    }

    @Throws(IOException::class)
    protected fun bytesFromJar(path: String): ByteArray {
        val `is` = inputStreamOfPath(path)
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(2048)
        var n: Int
        while (run {
                n = `is`.read(buffer)
                (n) >= 0
            })
            baos.write(buffer, 0, n)
        `is`.close()
        return baos.toByteArray()
    }

    @Throws(IOException::class)
    protected fun loadFromBytes(bytes: ByteArray, script: String): Prototype {
        val `is` = ByteArrayInputStream(bytes)
        return globals!!.loadPrototype(`is`, script, "b")
    }

    protected fun protoToString(p: Prototype): String {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        Print.ps = ps
        Print.printFunction(p, true)
        return baos.toString()
    }

}
