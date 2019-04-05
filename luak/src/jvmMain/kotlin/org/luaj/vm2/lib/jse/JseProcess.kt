/*******************************************************************************
 * Copyright (c) 2012 Luaj.org. All rights reserved.
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
package org.luaj.vm2.lib.jse

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/** Analog of Process that pipes input and output to client-specified streams.
 */
class JseProcess private constructor(
    internal val process: Process,
    stdin: InputStream?,
    stdout: OutputStream?,
    stderr: OutputStream?
) {
    @JvmField internal val input: Thread? = if (stdin == null) null else copyBytes(stdin, process.outputStream, null, process.outputStream)
    @JvmField internal val output: Thread? = if (stdout == null) null else copyBytes(process.inputStream, stdout, process.inputStream, null)
    @JvmField internal val error: Thread? = if (stderr == null) null else copyBytes(process.errorStream, stderr, process.errorStream, null)

    /** Construct a process around a command, with specified streams to redirect input and output to.
     *
     * @param cmd The command to execute, including arguments, if any
     * @param stdin Optional InputStream to read from as process input, or null if input is not needed.
     * @param stdout Optional OutputStream to copy process output to, or null if output is ignored.
     * @param stderr Optinoal OutputStream to copy process stderr output to, or null if output is ignored.
     * @throws IOException If the system process could not be created.
     * @see Process
     */

    constructor(
        cmd: Array<String>,
        stdin: InputStream,
        stdout: OutputStream,
        stderr: OutputStream
    ) : this(Runtime.getRuntime().exec(cmd), stdin, stdout, stderr) {
    }

    /** Construct a process around a command, with specified streams to redirect input and output to.
     *
     * @param cmd The command to execute, including arguments, if any
     * @param stdin Optional InputStream to read from as process input, or null if input is not needed.
     * @param stdout Optional OutputStream to copy process output to, or null if output is ignored.
     * @param stderr Optinoal OutputStream to copy process stderr output to, or null if output is ignored.
     * @throws IOException If the system process could not be created.
     * @see Process
     */

    constructor(
        cmd: String,
        stdin: InputStream?,
        stdout: OutputStream?,
        stderr: OutputStream?
    ) : this(Runtime.getRuntime().exec(cmd), stdin, stdout, stderr) {
    }

    /** Get the exit value of the process.  */
    fun exitValue(): Int {
        return process.exitValue()
    }

    /** Wait for the process to complete, and all pending output to finish.
     * @return The exit status.
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun waitFor(): Int {
        val r = process.waitFor()
        input?.join()
        output?.join()
        error?.join()
        process.destroy()
        return r
    }

    /** Create a thread to copy bytes from input to output.  */
    private fun copyBytes(
        input: InputStream,
        output: OutputStream, ownedInput: InputStream?,
        ownedOutput: OutputStream?
    ): Thread {
        val t = CopyThread(output, ownedOutput, ownedInput, input)
        t.start()
        return t
    }

    class CopyThread constructor(
        private val output: OutputStream, private val ownedOutput: OutputStream?,
        private val ownedInput: InputStream?, private val input: InputStream
    ) : Thread() {

        override fun run() {
            try {
                val buf = ByteArray(1024)
                var r: Int
                try {
                    while ((run { r = input.read(buf); r }) >= 0) {
                        output.write(buf, 0, r)
                    }
                } finally {
                    ownedInput?.close()
                    ownedOutput?.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}
