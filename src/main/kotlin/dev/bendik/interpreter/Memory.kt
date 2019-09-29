package dev.bendik.interpreter

fun writeMemory(process: Process, address: Long, value: Long, size: Int): Process {
    val ad = address.toInt()
    val bytes = longToBytes(value)
    for (i in 0 until size) {
        process.memory[ad + i] = bytes[i]
    }

    return process
}

fun readMemory(process: Process, address: Long, size: Int): Long {
    val ad = address.toInt()
    val bytes = process.memory.slice(ad..(ad + size))
    return bytesToLong(bytes.toByteArray(), size)
}

private fun longToBytes(value: Long): ByteArray {
    var v = value
    val result = ByteArray(8)
    for(i in 0 until 8) {
        result[7 - i] = (v and 0xFF).toByte()
        v = v shr 8
    }

    return result
}

private fun bytesToLong(value: ByteArray, size: Int): Long {
    var result = 0L
    for (i in 0 until size) {
        result = result shl 8
        result = result or (value[i].toLong() and 0xFF)
    }

    return result
}