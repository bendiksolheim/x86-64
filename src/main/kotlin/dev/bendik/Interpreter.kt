package dev.bendik

import dev.bendik.domain.*

fun interpret(process: Process): Long {
    return if (isDone(process)) {
        process.registers.RAX
    } else {
        val nextProcess = when (val instruction = process.instructions[process.registers.RIP.toInt()]) {
            is Mov -> mov(instruction, process)
            is Add -> add(instruction, process)
            is Sub -> sub(instruction, process)
            is Call -> call(instruction, process)
            is Ret -> ret(process)
            is Push -> push(instruction, process)
            is Pop -> pop(instruction, process)
        }

        interpret(nextProcess)
    }
}

fun mov(mov: Mov, process: Process): Process {
    val lhs = mov.lhs
    val rhs = mov.rhs
    val updated: Process = when (lhs) {
        is RegisterRef -> process.copy(registers = lhs.register.set(process.registers, interpretValue(process, rhs)))
        is MemoryRef -> writeMemory(process, lhs.register.get(process.registers) - lhs.offset, interpretValue(process, rhs), 8)
        is Literal -> process //ignore?
    }
    return updated.copy(registers = updated.registers.copy(RIP = updated.registers.RIP + 1))
}

fun add(add: Add, process: Process): Process {
    val lhs = add.lhs
    val rhs = add.rhs
    val updated = lhs.modify(process.registers) { it + interpretValue(process, rhs)}
    return process.copy(registers = updated.copy(RIP = updated.RIP + 1))
}

fun sub(sub: Sub, process: Process): Process {
    val lhs = sub.lhs
    val rhs = sub.rhs
    val updated = lhs.modify(process.registers) { it - interpretValue(process, rhs) }
    return process.copy(registers = updated.copy(RIP = updated.RIP + 1))
}

fun call(call: Call, process: Process): Process {
    val updated = process.registers.copy(RSP = process.registers.RSP - 1)
    writeMemory(process, updated.RSP, updated.RIP + 1, 8)
    return process.copy(registers = updated.copy(RIP = process.labels[call.lhs]!!))
}

fun ret(process: Process): Process {
    val value = readMemory(process, process.registers.RSP, 8)
    val updated = process.registers.copy(RSP = process.registers.RSP + 1)
    return process.copy(registers = updated.copy(RIP = value))
}

fun push(push: Push, process: Process): Process {
    val value = push.lhs.get(process.registers)
    val updated = process.registers.copy(RSP = process.registers.RSP - 1)
    writeMemory(process, updated.RSP, value, 8)
    return process.copy(registers = updated.copy(RIP = updated.RIP + 1))
}

fun pop(pop: Pop, process: Process): Process {
    val value = readMemory(process, process.registers.RSP, 8)
    val updated = process.registers.copy(RSP = process.registers.RSP + 1)
    val updated2 = pop.lhs.set(updated, value)
    return process.copy(registers = updated2.copy(RIP = updated2.RIP + 1))
}

fun interpretValue(process: Process, value: Reference): Long =
    when (value) {
        is RegisterRef -> value.register.get(process.registers)
        is Literal -> value.number
        is MemoryRef -> readMemory(process, value.register.get(process.registers) - value.offset, 8)
    }


fun isDone(process: Process) =
    process.registers.RIP == readMemory(process, process.memory.count().toLong() - 8 - 1, 8)