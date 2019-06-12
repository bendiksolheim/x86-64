package dev.bendik

import dev.bendik.domain.*

fun parse(program: List<String>): Pair<Map<String, Int>, List<Instruction>> {
    val labels = mutableMapOf<String, Int>()
    val instructions = mutableListOf<Instruction>()
    for (line in program) {
        if (line.contains(':')) {
            labels[line.split(':')[0]] = instructions.count()
        } else {
            val l = line.toUpperCase()
            val operator = l.split("""\s""".toRegex())[0]
            val operands = l.toUpperCase().substring(operator.count()).trim().split(',').map { it.trim() }
            instructions.add(parseInstruction(operator, operands))
        }
    }

    return Pair(labels, instructions)
}

fun parseInstruction(operator: String, operands: List<String>): Instruction =
    when (operator) {
        "MOV" -> Mov(
            parseReference(operands[0]),
            parseReference(operands[1])
        )
        "ADD" -> Add(
            Register.valueOf(operands[0]),
            parseReference(operands[1])
        )
        "PUSH" -> Push(Register.valueOf(operands[0]))
        "POP" -> Pop(Register.valueOf(operands[0]))
        "CALL" -> Call(operands[0])
        "RET" -> Ret
        else -> throw RuntimeException("Unknown instruction $operator")
    }