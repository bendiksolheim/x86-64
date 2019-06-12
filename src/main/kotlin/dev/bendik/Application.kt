package dev.bendik

import dev.bendik.domain.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KProperty0

fun isDirective(s: String) = s.startsWith('.')
fun isComment(s: String) = s.startsWith(';') || s.startsWith('#')
val isSource = compose(Boolean::not, (::isDirective or ::isComment or String::isEmpty))

fun removeComments(s: String) = s.split(';')[0].split('#')[0]

fun getSource(file: String) = Files.readAllLines(Paths.get(file))

fun parseInstruction(operator: String, operands: List<String>): Instruction =
    when (operator) {
        "MOV" -> Mov(parseReference(operands[0]), parseReference(operands[1]))
        "ADD" -> Add(Register.valueOf(operands[0]), parseReference(operands[1]))
        "PUSH" -> Push(Register.valueOf(operands[0]))
        "POP" -> Pop(Register.valueOf(operands[0]))
        "CALL" -> Call(operands[0])
        "RET" -> Ret
        else -> throw RuntimeException("Unknown instruction $operator")
    }

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

fun createProcess(program: Pair<Map<String, Int>, List<Instruction>>): Process {
    val registers: Map<Register, Long> = Register.values().associate { Pair(it, 0L) }
    val memory = ByteArray(1000)
    return Process(registers, memory, program.second, program.first)
}


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No filename given")
    } else {
        val source = getSource(args[0])
        val program = source
            .map(String::trim)
            .filter (::isSource)
            .map (::removeComments)

        val parsed = parse(program)
        val process = createProcess(parsed)
        println(process.labels)
        println(process.instructions)
    }
}

private fun <T> Iterable<T>.filter(predicate: KProperty0<(T) -> Boolean>): Iterable<T> =
    this.filterTo(ArrayList(), predicate.get())


