package dev.bendik.parser

import dev.bendik.interpreter.RegisterLens
import dev.bendik.interpreter.getRegisterLens

sealed class Instruction
data class Mov(val lhs: Reference, val rhs: Reference): Instruction()
data class Add(val lhs: RegisterLens, val rhs: Reference): Instruction()
data class Sub(val lhs: RegisterLens, val rhs: Reference): Instruction()
data class Push(val lhs: RegisterLens): Instruction()
data class Pop(val lhs: RegisterLens): Instruction()
data class Call(val lhs: String): Instruction()
object Ret : Instruction()

fun parseInstruction(operator: String, operands: List<String>): Instruction =
    when (operator) {
        "MOV" -> Mov(
            parseReference(operands[0]),
            parseReference(operands[1])
        )
        "ADD" -> Add(
            getRegisterLens(operands[0]),
            parseReference(operands[1])
        )
        "SUB" -> Sub(
            getRegisterLens(operands[0]),
            parseReference(operands[1])
        )
        "PUSH" -> Push(getRegisterLens(operands[0]))
        "POP" -> Pop(getRegisterLens(operands[0]))
        "CALL" -> Call(operands[0])
        "RET" -> Ret
        else -> throw RuntimeException("Unknown instruction $operator")
    }