package dev.bendik.domain

sealed class Instruction
data class Mov(val lhs: Reference, val rhs: Reference): Instruction()
data class Add(val lhs: RegisterLens, val rhs: Reference): Instruction()
data class Push(val lhs: RegisterLens): Instruction()
data class Pop(val lhs: RegisterLens): Instruction()
data class Call(val lhs: String): Instruction()
object Ret : Instruction()