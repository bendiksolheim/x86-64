package dev.bendik

import arrow.core.andThen
import dev.bendik.preprocessor.preprocess
import dev.bendik.interpreter.createProcess
import dev.bendik.interpreter.interpret
import dev.bendik.parser.parse
import dev.bendik.util.readSource

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No filename given")
    } else {
        println(program(args[0]))
    }
}

val program = ::readSource andThen
        ::preprocess andThen
        ::parse andThen
        ::createProcess andThen
        ::interpret