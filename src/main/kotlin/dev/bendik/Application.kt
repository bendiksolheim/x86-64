package dev.bendik

import dev.bendik.domain.*
import java.nio.file.Files
import java.nio.file.Paths

fun getSource(file: String): List<String> = Files.readAllLines(Paths.get(file))

fun createProcess(program: Pair<Map<String, Int>, List<Instruction>>): Process {
    val registers: Map<Register, Long> = Register.values().associate { Pair(it, 0L) }
    val memory = ByteArray(1000)
    return Process(registers, memory, program.second, program.first)
}


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



