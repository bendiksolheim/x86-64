package dev.bendik.interpreter

import java.lang.RuntimeException
import kotlin.system.exitProcess

fun getSyscall(number: Long): (Process) -> Unit {
    val syscall = syscalls[number.toInt()]
    if (syscall != null) {
        return syscall
    } else {
        throw RuntimeException("Unknown syscall $number")
    }
}

private val syscalls = mapOf<Int, (Process) -> Unit>(
    60 to ::exit,
    1 to ::write
)

private fun exit(process: Process) {
    val exitCode = process.registers.RDI
    exitProcess(exitCode.toInt())
}

private fun write(process: Process) {
    val address = process.registers.RSI
    val bytes = process.registers.RDX
    println("Bytes: $bytes")
    val fileDescriptor = process.registers.RDI
    val file = process.fileDescriptors[fileDescriptor.toInt()]!!
    for (offset in 0 until bytes) {
        val char = readMemory(process, address + offset, 1)
        file.write(char.toInt())
    }
}
