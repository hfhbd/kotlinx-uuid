/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.uuid.Uuid

/**
 * UUID [numeric version](https://tools.ietf.org/html/rfc4122#section-4.1.3) in range `[0..15]
 */
public val Uuid.versionNumber: Int get() = toLongs { mostSignificantBits, _ -> (mostSignificantBits and 0xf000L shr 12).toInt() }

/**
 * UUID variant in range `[0..7]`, similar to version
 */
public val Uuid.variant: Int get() = toLongs { _, leastSignificantBits -> (leastSignificantBits ushr 61).toInt() }

/**
 * UUID variant specified and documented by the RFC
 */
public val Uuid.isRfcVariant: Boolean get() = variant == 4 || variant == 5

/**
 * Check the [spec] string to conform to UUID
 * @return `true` if the [spec] string is a UUID string
 */
public fun Uuid.Companion.isValidUUIDString(spec: String): Boolean = try {
    parse(spec)
    true
} catch (_: IllegalArgumentException) {
    false
}

/**
 * Convert this String to a [Uuid], or throws a [IllegalArgumentException] if [this] is a malformed UUID.
 */
public fun String.toUuid(): Uuid = Uuid.parse(this)

/**
 * Convert this String to a [Uuid], or returns null if [this] is a malformed UUID.
 */
public fun String.toUUIDOrNull(): Uuid? = try {
    Uuid.parse(this)
} catch (_: IllegalArgumentException) {
    null
}
