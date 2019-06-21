package dev.bendik.domain

sealed class Reference
data class RegisterRef(val register: RegisterLens) : Reference()
data class MemoryRef(val register: RegisterLens, val offset: Int): Reference()
data class Literal(val number: Long): Reference()

fun parseReference(ref: String): Reference =
    when {
        isRegister(ref) -> RegisterRef(getRegisterLens(ref))
        ref.contains("QWORD") -> {
            val result = """QWORD PTR \[([A-Z]+) - ([0-9]+)]""".toRegex().matchEntire(ref)
                ?: throw RuntimeException("Unknown reference [$ref]")
            val (register, offset) = result.destructured
            MemoryRef(getRegisterLens(register), offset.toInt())
        }
        else -> Literal(ref.toLong())
    }
