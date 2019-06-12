package dev.bendik.domain

sealed class Instruction
data class Mov(val lhs: Reference, val rhs: Reference): Instruction()
data class Add(val lhs: Register, val rhs: Reference): Instruction()
data class Push(val lhs: Register): Instruction()
data class Pop(val lhs: Register): Instruction()
data class Call(val lhs: String): Instruction()
object Ret : Instruction()