package dev.bendik.domain

import java.lang.RuntimeException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

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

/**
 * Uses the name of a register to create a lens pointing to a specific register
 * Usage:
 * val lens = getRegisterLens("RIP")
 * lens.get(registers)
 * lens.set(registers, newValue)
 * lens.modify(registers) { it + newValue }
 */
fun getRegisterLens(register: String): RegisterLens = RegisterLens(
        { registers -> getRegister(registers, register).get(registers)},
        { registers, newValue ->
            val copy = registers::class.memberFunctions.first { it.name == "copy"}
            val instanceParam = copy.instanceParameter!!
            val regParam = copy.parameters.first { it.name == register}
            copy.callBy(mapOf(instanceParam to registers, regParam to newValue)) as Registers
        },
        { registers, updateFn ->
            val reg = getRegister(registers, register)
            val oldValue = reg.get(registers)
            val newValue = updateFn(oldValue)
            val copy = registers::class.memberFunctions.first { it.name == "copy"}
            val instanceParam = copy.instanceParameter!!
            val regParam = copy.parameters.first { it.name == register}
            copy.callBy(mapOf(instanceParam to registers, regParam to newValue)) as Registers
        }
    )

@Suppress("UNCHECKED_CAST")
fun getRegister(registers: Registers, register: String): KProperty1<Registers, Long> {
    return registers::class.memberProperties
        .first { it.name == register } as KProperty1<Registers, Long>
}

class RegisterLens (
    val get: (Registers) -> Long,
    val set: (Registers, Long) -> Registers,
    val modify: (Registers, (Long) -> Long) -> Registers
)