package dev.bendik.util

import java.nio.file.Files
import java.nio.file.Paths

fun readSource(file: String): List<String> = Files.readAllLines(Paths.get(file))