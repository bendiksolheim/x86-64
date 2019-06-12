package dev.bendik

inline infix fun <IP, R, P1> ((IP) -> R).compose(crossinline f: (P1) -> IP): (P1) -> R = { p1: P1 -> this(f(p1)) }
infix fun <A, B, C> ((A) -> B).pipe(other: (B) -> C) : (A) -> C = { other(this(it)) }