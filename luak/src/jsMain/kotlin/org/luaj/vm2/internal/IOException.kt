package org.luaj.vm2.internal

actual open class IOException : Exception {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

actual open class EOFException : IOException {
    actual constructor() : super()
    actual constructor(message: String) : super(message)
}

