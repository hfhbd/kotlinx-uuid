/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

internal class UUIDFormatException(override val message: String) : Exception(message)

internal fun parseUUID(text: String): UUID {
    var textIndex = skipCharacters(text, 0, '{')

    // time low
    var timeStampAndVersionRaw = parseOctetStride(text, textIndex, 4, 32)
    textIndex = skipCharacters(text, textIndex + 8, '-')

    // time mid
    timeStampAndVersionRaw = timeStampAndVersionRaw or
        parseOctetStride(text, textIndex, 2, 16)
    textIndex = skipCharacters(text, textIndex + 4, '-')

    // time high and version
    timeStampAndVersionRaw = timeStampAndVersionRaw or
        parseOctetStride(text, textIndex, 2, 0)
    textIndex = skipCharacters(text, textIndex + 4, '-')

    // variant + clock
    var clockSequenceVariantAndNodeRaw = parseOctetStride(text, textIndex, 2, 48)
    textIndex = skipCharacters(text, textIndex + 4, '-')

    // node
    clockSequenceVariantAndNodeRaw = clockSequenceVariantAndNodeRaw or
        parseOctetStride(text, textIndex, 6, 0)

    textIndex = skipCharacters(text, textIndex + 12, '}')

    if (textIndex < text.length) {
        error("extra trailing characters ${text.substring(textIndex)}", text, textIndex)
    }

    return UUID.create(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

private fun parseOctetStride(
    text: String,
    textIndex: Int,
    numberOfOctets: Int,
    shift: Int
): Long {
    if (text.length - textIndex < numberOfOctets * 2) {
        errorTooShort(text)
    }

    var result = 0L
    repeat(numberOfOctets * 2) { i ->
        val halfByte = when (val character = text[textIndex + i]) {
            in '0'..'9' -> character - '0'
            in 'a'..'f' -> character - 'a' + 10
            in 'A'..'F' -> character - 'A' + 10
            else -> error("Unexpected octet character $character", text, textIndex + i)
        }
        result = result shl 4 or halfByte.toLong()
    }

    return result shl shift
}

private fun errorTooShort(text: String) {
    error("UUID string is too short", text, -1)
}

private fun skipCharacters(text: String, startIndex: Int, a: Char): Int {
    for (index in startIndex until text.length) {
        val character = text[index]
        if (character == a || character == ' ' || character == '\t') {
            continue
        }
        return index
    }
    return text.length
}

private fun error(message: String, text: String, index: Int): Nothing {
    throw UUIDFormatException(
        when (index) {
            -1 -> "Failed to parse UUID $text: $message"
            else -> "Failed to parse UUID $text at position $index: $message"
        }
    )
}
