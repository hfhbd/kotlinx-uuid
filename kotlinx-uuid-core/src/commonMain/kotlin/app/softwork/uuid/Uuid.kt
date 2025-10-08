/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import kotlin.uuid.Uuid

/**
 * Creates an instance of [Uuid] from components
 */
public fun Uuid.Companion.from(
    versionNumber: Int,
    timeStamp: Long,
    clockSequence: Int,
    node: Long,
    variant: Int = 5
): Uuid {
    require(versionNumber in 0..15)
    require(variant in 0..7)
    require(timeStamp in 0L until (1L shl 60))
    require(node in 0L until (1L shl 48))
    require(clockSequence in 0 until (1 shl 13))

    val timeStampAndVersionRaw = (timeStamp shl 32) or (timeStamp and 0xffff00000000L shr 16) or
        (timeStamp shr 48) or (versionNumber.toLong() shl 12)
    val clockSequenceVariantAndNodeRaw = (clockSequence.toLong() shl 48) or
        (variant.toLong() shl 61) or node

    return fromLongs(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * A 60-bits non-negative number. Depending on the Uuid [version] it could have different semantics:
 * - UTC time
 * - a number constructed from the namespace
 * - a random number
 */
public val Uuid.timeStamp: Long
    get() = toLongs { timeStampAndVersionRaw, _ ->
        (timeStampAndVersionRaw ushr 32) or
            (timeStampAndVersionRaw and 0xffff0000L shl 16) or
            (timeStampAndVersionRaw and 0x0fffL shl 48)
    }

/**
 * 13-bits non-negative number representing a sequence number
 * or a random number depending on Uuid [version] and [variant].
 */
public val Uuid.clockSequence: Int
    get() = toLongs { _, clockSequenceVariantAndNodeRaw -> (clockSequenceVariantAndNodeRaw shr 48 and 0x1fff).toInt() }

/**
 * Uuid [numeric version](https://tools.ietf.org/html/rfc4122#section-4.1.3) in range `[0..15]`
 */
public val Uuid.version: Int
    get() = toLongs { timeStampAndVersionRaw, _ ->
        (timeStampAndVersionRaw and 0xf000L shr 12).toInt()
    }

/**
 * Uuid variant in range `[0..7]`, similar to [version]
 */
public val Uuid.variant: Int
    get() = toLongs { _, clockSequenceVariantAndNodeRaw ->
        (clockSequenceVariantAndNodeRaw ushr 61).toInt()
    }

/**
 * Uuid [variant] specified and documented by the RFC
 */
public val Uuid.isRfcVariant: Boolean get() = variant == 4 || variant == 5

/**
 * Node Uuid part, a 48-bit non-negative number.
 * Depending on [version] and [variant] it could be one of the following:
 * - a MAC address
 * - a random number
 * - a value constructed from a namespace
 */
public val Uuid.node: Long
    get() = toLongs { _, clockSequenceVariantAndNodeRaw -> clockSequenceVariantAndNodeRaw and 0xffffffffffffL }

/**
 * Check the [spec] string to conform to Uuid
 * @return `true` if the [spec] string is a Uuid string
 */
@Deprecated("Use Uuid.parseOrNull(spec) != null instead", ReplaceWith("Uuid.parseOrNull(spec) != null"))
public fun Uuid.Companion.isValidUuidString(spec: String): Boolean = Uuid.parseOrNull(spec) != null

/**
 * Convert this String to a [Uuid], or throws a [IllegalArgumentException] if [this] is a malformed Uuid.
 */
@Deprecated("Use Uuid.parse instead", ReplaceWith("Uuid.parse(this)"))
public fun String.toUuid(): Uuid = Uuid.parse(this)

/**
 * Convert this String to a [Uuid], or returns null if [this] is a malformed Uuid.
 */
@Deprecated("Use Uuid.parseOrNull instead", ReplaceWith("Uuid.parseOrNull(this)"))
public fun String.toUuidOrNull(): Uuid? = Uuid.parseOrNull(this)

public val Uuid.Companion.MAX: Uuid get() = fromLongs(-1, -1)
