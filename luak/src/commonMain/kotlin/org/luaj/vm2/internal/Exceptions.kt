package org.luaj.vm2.internal

expect open class IOException : Exception {
    constructor()
    constructor(message: String)
}

expect open class EOFException : IOException {
    constructor()
    constructor(message: String)
}

