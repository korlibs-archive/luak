package com.soywiz.luak.compat.java.io

open class IOException(message: String = "IOException") : Exception(message)
open class EOFException(message: String = "EOFException") : IOException(message)
open class UnsupportedEncodingException(message: String = "UnsupportedEncodingException") : IOException(message)
