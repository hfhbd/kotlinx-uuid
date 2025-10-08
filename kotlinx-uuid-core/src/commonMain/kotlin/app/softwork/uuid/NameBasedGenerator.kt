/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("DEPRECATION")

package app.softwork.uuid

import app.softwork.uuid.internal.SHA1
import kotlin.uuid.Uuid

/**
 * Generates a [Uuid] instance by [namespace] and [name] applying SHA-1.
 * It doesn't use any random source, so it will produce the same result for
 * the same input values.
 *
 * See [RFC4122 sec 4.3](https://tools.ietf.org/html/rfc4122#section-4.3)
 */
@Deprecated("The internal SHA1 implementation won't get any updates and will be removed in the next release.")
public fun Uuid.Companion.generateUuid(namespace: Uuid, name: String): Uuid {
    val hash = sha1 {
        update(namespace.toByteArray())
        update(name.encodeToByteArray())
    }

    return generateUuidByHash(hash)
}

/**
 * Generates a [Uuid] instance by input [bytes] applying SHA-1.
 * It doesn't use any random source, so it will produce the same result for
 * the same input values.
 *
 * See [RFC4122 sec 4.3](https://tools.ietf.org/html/rfc4122#section-4.3)
 *
 * It is recommended to use generateUuid(namespace, name) instead while this function
 * is more for java.util.UUID parity.
 */
@Deprecated("The internal SHA1 implementation won't get any updates and will be removed in the next release.")
public fun Uuid.Companion.generateUuid(bytes: ByteArray): Uuid {
    val hash = sha1 {
        update(bytes)
    }

    return generateUuidByHash(hash)
}

@Suppress("DEPRECATION")
private inline fun sha1(builder: SHA1.() -> Unit): ByteArray {
    val sha1 = SHA1()
    builder(sha1)
    return sha1.final()
}

@Suppress("DEPRECATION")
private fun generateUuidByHash(hashBytes: ByteArray): Uuid {
    hashBytes[6] = (hashBytes[6].toInt() and 0x0f or 80).toByte()
    hashBytes[8] = (hashBytes[8].toInt() and 0x3f or 0x80).toByte()

    return Uuid.fromByteArray(hashBytes.copyOf(Uuid.SIZE_BYTES))
}
