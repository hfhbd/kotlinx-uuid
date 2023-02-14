package kotlinx.uuid.internal

internal fun ByteArray.toInt(): Int {
    var result = 0
    for (byte in this) {
        result = (result or byte.toInt()) shl Byte.SIZE_BITS
    }
    return result
}
