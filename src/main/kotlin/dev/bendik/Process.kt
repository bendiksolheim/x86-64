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

fun writeMemory(process: Process, address: Long, value: Long, size: Int) {
    val ad = address.toInt()
    val bytes = longToBytes(value)
    for (i in 0 until size) {
        process.memory[ad + i] = bytes[i]
    }
}

fun readMemory(process: Process, address: Long, size: Int): Long {
    val ad = address.toInt()
    val bytes = process.memory.slice(ad..(ad + size))
    return bytesToLong(bytes.toByteArray())
}

private fun longToBytes(value: Long): ByteArray {
    var v = value
    val result = ByteArray(8)
    for(i in 0 until 8) {
        result[7 - i] = (v and 0xFF).toByte()
        v = v shr 8
    }

    return result
}

private fun bytesToLong(value: ByteArray): Long {
    var result = 0L
    for (i in 0 until 8) {
        result = result shl 8
        result = result or (value[i].toLong() and 0xFF)
    }

    return result
}