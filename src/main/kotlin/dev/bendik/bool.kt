package dev.bendik

infix fun <T> ((T) -> Boolean).or(g: (T) -> Boolean): (T) -> Boolean = { s ->
    g(s) || this(s)
}