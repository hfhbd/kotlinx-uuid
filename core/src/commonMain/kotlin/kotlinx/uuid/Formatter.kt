/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

/**
 * Renders UUID in the RFC format: five groups of hexademical parts separated with
 * minus characters and surrounded with curly brackets if [includeBrackets] is `true`,
 * without spaces.
 * `{1b3e4567-e99b-13d3-a476-446657420000}`
 */
internal fun formatUUID(
    timeStampAndVersionRaw: Long,
    clockSequenceVariantAndNodeRaw: Long,
    includeBrackets: Boolean
): String = buildString(38) {
    if (includeBrackets) {
        append('{')
    }
    dumpHex(timeStampAndVersionRaw ushr 32, 4, this)
    append('-')
    dumpHex(timeStampAndVersionRaw ushr 16 and 0xffff, 2, this)
    append('-')
    dumpHex(timeStampAndVersionRaw and 0xffff, 2, this)
    append('-')
    dumpHex(clockSequenceVariantAndNodeRaw shr 48 and 0xffff, 2, this)
    append('-')
    dumpHex(clockSequenceVariantAndNodeRaw and 0xffffffffffffL, 6, this)
    if (includeBrackets) {
        append('}')
    }
}

internal fun dumpHex(value: Long, numberOfOctets: Int, out: StringBuilder) {
    repeat(numberOfOctets) { index ->
        val octet = value ushr ((numberOfOctets - index - 1) * 8)
        dumpHalfByte(octet.toInt() shr 4, out)
        dumpHalfByte(octet.toInt(), out)
    }
}

private fun dumpHalfByte(value: Int, out: StringBuilder) {
    val half = value and 0x0f
    out.append(
        when {
            half <= 9 -> '0' + half
            else -> 'a' + half - 10
        }
    )
}
