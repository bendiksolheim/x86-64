package dev.bendik

import dev.bendik.domain.*

fun interpret(process: Process): Long {
    return if (isDone(process)) {
        process.registers[Register.RAX]!!
    } else {
        val instruction = process.instructions[process.registers[Register.RIP]!!.toInt()]
        when (instruction) {
            is Mov -> mov(instruction, process)
            is Add -> add(instruction, process)
            is Call -> call(instruction, process)
            is Ret -> ret(process)
            is Push -> push(instruction, process)
            is Pop -> pop(instruction, process)
        }

        interpret(process)
    }
}

fun mov(mov: Mov, process: Process) {
    val lhs = mov.lhs
    val rhs = mov.rhs
    when (lhs) {
        is RegisterRef -> process.registers[lhs.register] = interpretValue(process, rhs)
        is MemoryRef -> writeMemory(process, process.registers[lhs.register]!! - lhs.offset, interpretValue(process, rhs), 8)
    }
    process.registers[Register.RIP] = process.registers[Register.RIP]!! + 1
}

fun add(add: Add, process: Process) {
    val lhs = add.lhs
    val rhs = add.rhs
    process.registers[lhs] = process.registers[lhs]!! + interpretValue(process, rhs)
    process.registers[Register.RIP] = process.registers[Register.RIP]!! + 1
}

fun call(call: Call, process: Process) {
    process.registers[Register.RSP] = process.registers[Register.RSP]!! - 1
    writeMemory(process, process.registers[Register.RSP]!!, process.registers[Register.RIP]!! + 1, 8)
    process.registers[Register.RIP] = process.labels[call.lhs]!!
}

fun ret(process: Process) {
    val value = readMemory(process, process.registers[Register.RSP]!!, 8)
    process.registers[Register.RSP] = process.registers[Register.RSP]!! + 1
    process.registers[Register.RIP] = value
}

fun push(push: Push, process: Process) {
    val value = process.registers[push.lhs]!!
    process.registers[Register.RSP] = process.registers[Register.RSP]!! - 1
    writeMemory(process, process.registers[Register.RSP]!!, value, 8)
    process.registers[Register.RIP] = process.registers[Register.RIP]!! + 1
}

fun pop(pop: Pop, process: Process) {
    val value = readMemory(process, process.registers[Register.RSP]!!, 8)
    process.registers[Register.RSP] = process.registers[Register.RSP]!! + 1
    process.registers[pop.lhs] = value
    process.registers[Register.RIP] = process.registers[Register.RIP]!! + 1
}

fun interpretValue(process: Process, value: Reference): Long =
    when (value) {
        is RegisterRef -> process.registers[value.register]!!
        is Literal -> value.number
        is MemoryRef -> readMemory(process, process.registers[value.register]!! - value.offset, 8)
    }


fun isDone(process: Process) =
    process.registers[Register.RIP] == readMemory(process, process.memory.count().toLong() - 8 - 1, 8)