/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.random.*
import kotlin.uuid.Uuid

/**
 * Generates a random UUID v4 using the specified [random] source.
 */
public fun Uuid.Companion.random(random: Random): Uuid {
    val randomBytes = random.nextBytes(16)
    return uuidFromRandomBytes(randomBytes)
}

// Copied from stdlib
private fun uuidFromRandomBytes(randomBytes: ByteArray): Uuid {
    randomBytes[6] = (randomBytes[6].toInt() and 0x0f).toByte() /* clear version        */
    randomBytes[6] = (randomBytes[6].toInt() or 0x40).toByte() /* set to version 4     */
    randomBytes[8] = (randomBytes[8].toInt() and 0x3f).toByte() /* clear variant        */
    randomBytes[8] = (randomBytes[8].toInt() or 0x80).toByte() /* set to IETF variant  */
    return Uuid.fromByteArray(randomBytes)
}

/**
 * Generates a random UUID v4 using this [Random] instance.
 */
public fun Random.nextUuid(): Uuid = Uuid.random(this)
