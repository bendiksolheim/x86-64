package dev.bendik.parser

fun parse(program: List<String>): ParseResult {
    val labels = mutableMapOf<String, Long>()
    val instructions = mutableListOf<Instruction>()
    for (line in program) {
        if (line.contains(':')) {
            labels[line.split(':')[0]] = instructions.count().toLong()
        } else {
            val l = line.toUpperCase()
            val operator = l.split("""\s""".toRegex())[0]
            val operands = l.toUpperCase().substring(operator.count()).trim().split(',').map { it.trim() }
            instructions.add(parseInstruction(operator, operands))
        }
    }

    return ParseResult(labels, instructions)
}

