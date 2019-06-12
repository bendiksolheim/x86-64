package dev.bendik

import dev.bendik.domain.*
import java.nio.file.Files
import java.nio.file.Paths

fun getSource(file: String): List<String> = Files.readAllLines(Paths.get(file))

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No filename given")
    } else {
        val pipeline = ::getSource pipe
                ::preprocess pipe
                ::parse pipe
                ::createProcess

        val process = pipeline(args[0])
        println(process.labels)
        println(process.instructions)
    }
}



