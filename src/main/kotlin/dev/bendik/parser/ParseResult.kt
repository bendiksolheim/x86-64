package dev.bendik.parser

data class ParseResult(val labels: Map<String, Long>,
                       val instructions: List<Instruction>)