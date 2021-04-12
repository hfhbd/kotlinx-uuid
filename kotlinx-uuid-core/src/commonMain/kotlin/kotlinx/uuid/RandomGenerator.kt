/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.random.*

/**
 * Generates a random UUID v4 using the specified [random] source.
 * It uses by default a [SecureRandom] instance.
 */
public fun UUID.Companion.generateUUID(random: Random = SecureRandom): UUID {
    // set version 4 (random)
    val timeStampAndVersionRaw = random.nextLong() and -0xf001L or 0x4000L

    // set variant to 4 or 5
    // we keep the lower variant bit random as it is defined as "don't care"
    val clockSequenceVariantAndNodeRaw: Long = random.nextLong() and
        0x3fffffffffffffffL or (0x80L shl 0x38)

    return create(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * Generates a random UUID v4 using this [Random] instance.
 */
public fun Random.nextUUID(): UUID = UUID.generateUUID(this)
