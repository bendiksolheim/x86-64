package dev.bendik.domain

data class ParseResult(val labels: Map<String, Int>,
                       val instructions: List<Instruction>)