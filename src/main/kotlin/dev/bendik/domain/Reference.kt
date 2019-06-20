package dev.bendik.domain

import arrow.syntax.function.memoize
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
val getRegisterLens: (register: String) -> RegisterLens = { register: String ->
        val reg = findRegister(register)
        val regParam = copy.parameters.first { it.name == register }

        RegisterLens(
            { registers -> reg.get(registers) },
            { registers, newValue ->
                copy.callBy(mapOf(instanceParam to registers, regParam to newValue)) as Registers
            },
            { registers, updateFn ->
                val oldValue = reg.get(registers)
                val newValue = updateFn(oldValue)
                copy.callBy(mapOf(instanceParam to registers, regParam to newValue)) as Registers
            }
        )
    }.memoize()


private val copy = Registers::class.memberFunctions.first { it.name == "copy" }
private val instanceParam = copy.instanceParameter!!

@Suppress("UNCHECKED_CAST")
private fun findRegister(register: String): KProperty1<Registers, Long> =
        Registers::class.memberProperties
            .first { it.name == register } as KProperty1<Registers, Long>

class RegisterLens (
    val get: (Registers) -> Long,
    val set: (Registers, Long) -> Registers,
    val modify: (Registers, (Long) -> Long) -> Registers
    )