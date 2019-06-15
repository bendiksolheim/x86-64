package dev.bendik.domain

data class ParseResult(val labels: Map<String, Long>,
                       val instructions: List<Instruction>)