package dev.bendik

import arrow.core.andThen
import dev.bendik.preprocessor.preprocess
import dev.bendik.interpreter.createProcess
import dev.bendik.interpreter.interpret
import dev.bendik.parser.parse
import java.nio.file.Files
import java.nio.file.Paths

fun getSource(file: String): List<String> = Files.readAllLines(Paths.get(file))

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No filename given")
    } else {
        val pipeline = ::getSource andThen
                ::preprocess andThen
                ::parse andThen
                ::createProcess andThen
                ::interpret

        val result = pipeline(args[0])
        println(result)
    }
}



