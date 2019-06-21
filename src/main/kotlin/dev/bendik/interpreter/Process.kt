package dev.bendik.interpreter

import dev.bendik.parser.Instruction
import dev.bendik.parser.ParseResult

data class Process(val registers: Registers,
                   val memory: ByteArray,
                   val instructions: List<Instruction>,
                   val labels: Map<String, Long>) {
    //Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Process

        if (registers != other.registers) return false
        if (!memory.contentEquals(other.memory)) return false
        if (instructions != other.instructions) return false
        if (labels != other.labels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = registers.hashCode()
        result = 31 * result + memory.contentHashCode()
        result = 31 * result + instructions.hashCode()
        result = 31 * result + labels.hashCode()
        return result
    }

}

fun createProcess(program: ParseResult): Process {
    val memory = ByteArray(1000)
    val registers = Registers(
        RIP = program.labels["_main"]!!,
        RSP = memory.count().toLong() - 8 - 1
    )
    val process = Process(registers, memory, program.instructions, program.labels)
    writeMemory(process, registers.RSP, registers.RSP, 8)

    return process
}