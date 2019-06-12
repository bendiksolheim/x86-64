package dev.bendik.domain

enum class Register {
    //Mutable
    RDI,
    RSI,
    RSP,
    RBP,
    RAX,
    RBX,
    RCX,
    RDX,
    R8,
    R9,
    R10,
    R11,
    R12,
    R13,
    R14,
    R15,

    // Immutable
    RIP,
    CS,
    DS,
    FS,
    SS,
    ES,
    GS,
    CF,
    ZF,
    PF,
    AF,
    SF,
    TF,
    IF,
    DF,
    OF;

    companion object {
        fun isRegister(register: String): Boolean =
            try {
                Register.valueOf(register)
                true
            } catch (e: Exception) {
                false
            }
    }
}

