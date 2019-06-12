package dev.bendik

import dev.bendik.domain.Instruction
import dev.bendik.domain.Register

data class Process(val registers: Map<Register, Long>,
                   val memory: ByteArray,
                   val instructions: List<Instruction>,
                   val labels: Map<String, Int>) {

    // Generated
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

    // Generated
    override fun hashCode(): Int {
        var result = registers.hashCode()
        result = 31 * result + memory.contentHashCode()
        result = 31 * result + instructions.hashCode()
        result = 31 * result + labels.hashCode()
        return result
    }
}

fun createProcess(program: Pair<Map<String, Int>, List<Instruction>>): Process {
    val registers: Map<Register, Long> = Register.values().associate { Pair(it, 0L) }
    val memory = ByteArray(1000)
    return Process(registers, memory, program.second, program.first)
}