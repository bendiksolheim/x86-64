package dev.bendik.interpreter

import arrow.syntax.function.memoize
import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

data class Registers(
    val RDI: Long = 0,
    val RSI: Long = 0,
    val RSP: Long = 0,
    val RBP: Long = 0,
    val RAX: Long = 0,
    val RBX: Long = 0,
    val RCX: Long = 0,
    val RDX: Long = 0,
    val R8: Long = 0,
    val R9: Long = 0,
    val R10: Long = 0,
    val R11: Long = 0,
    val R12: Long = 0,
    val R13: Long = 0,
    val R14: Long = 0,
    val R15: Long = 0,
    val RIP: Long = 0,
    val CS: Long = 0,
    val DS: Long = 0,
    val FS: Long = 0,
    val SS: Long = 0,
    val ES: Long = 0,
    val GS: Long = 0,
    val CF: Long = 0,
    val ZF: Long = 0,
    val PF: Long = 0,
    val AF: Long = 0,
    val SF: Long = 0,
    val TF: Long = 0,
    val IF: Long = 0,
    val DF: Long = 0,
    val OF: Long = 0
)

fun isRegister(value: String): Boolean =
    Registers::class.memberProperties.any { it.name == value }

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
            copy.callBy(
                mapOf(
                    instanceParam to registers,
                    regParam to newValue
                )
            ) as Registers
        },
        { registers, updateFn ->
            val oldValue = reg.get(registers)
            val newValue = updateFn(oldValue)
            copy.callBy(
                mapOf(
                    instanceParam to registers,
                    regParam to newValue
                )
            ) as Registers
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