package com.soywiz.classext

import com.soywiz.korio.lang.*
import kotlin.reflect.*

val KClass<*>.portableLongName get() = this.portableSimpleName
