package dev.bendik.domain

import dev.bendik.compose
import dev.bendik.or
import kotlin.reflect.KProperty0

fun isDirective(s: String) = s.startsWith('.')
fun isComment(s: String) = s.startsWith(';') || s.startsWith('#')
val isSource = Boolean::not compose (::isDirective or ::isComment or String::isEmpty)

fun removeComments(s: String) = s.split(';')[0].split('#')[0]

fun preprocess(source: List<String>): List<String> =
    source
        .map(String::trim)
        .filter (::isSource)
        .map (::removeComments)


private fun <T> Iterable<T>.filter(predicate: KProperty0<(T) -> Boolean>): Iterable<T> =
    this.filterTo(ArrayList(), predicate.get())