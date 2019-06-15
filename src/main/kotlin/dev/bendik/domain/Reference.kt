package dev.bendik.domain

import java.lang.RuntimeException

sealed class Reference
data class RegisterRef(val register: Register) : Reference()
data class MemoryRef(val register: Register, val offset: Int): Reference()
data class Literal(val number: Long): Reference()

fun parseReference(ref: String): Reference =
    when {
        Register.isRegister(ref) -> RegisterRef(Register.valueOf(ref))
        ref.contains("QWORD") -> {
            val result = """QWORD PTR \[([A-Z]+) - ([0-9]+)]""".toRegex().matchEntire(ref)
                ?: throw RuntimeException("Unknown reference [$ref]")
            val (register, offset) = result.destructured
            MemoryRef(Register.valueOf(register), offset.toInt())
        }
        else -> Literal(ref.toLong())
    }