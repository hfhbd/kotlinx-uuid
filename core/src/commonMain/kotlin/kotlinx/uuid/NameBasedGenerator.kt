/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.uuid.internal.*
import kotlin.experimental.*

/**
 * Generates a [UUID] instance by [namespace] and [name] applying SHA-1.
 * It doesn't use any random source so it will produce the same result for
 * the same input values.
 *
 * See [RFC4122 sec 4.3](https://tools.ietf.org/html/rfc4122#section-4.3)
 */
public fun UUID.Companion.generateUUID(namespace: UUID, name: String): UUID {
    val hash = sha1 {
        update(namespace.encodeToByteArray())
        update(name.encodeToByteArray())
    }

    return generateUUIDByHash(hash, version = UUID.Version.NAME_BASED_SHA1.id)
}

/**
 * Generates a [UUID] instance by input [bytes] applying SHA-1.
 * It doesn't use any random source so it will produce the same result for
 * the same input values.
 *
 * See [RFC4122 sec 4.3](https://tools.ietf.org/html/rfc4122#section-4.3)
 *
 * It is recommended to use generateUUID(namespace, name) instead while this function
 * is more for java.util.UUID parity.
 */
public fun UUID.Companion.generateUUID(bytes: ByteArray): UUID {
    val hash = sha1 {
        update(bytes)
    }

    return generateUUIDByHash(hash, version = UUID.Version.NAME_BASED_SHA1.id)
}

private inline fun sha1(builder: SHA1.() -> Unit): ByteArray {
    val sha1 = SHA1()
    builder(sha1)
    return sha1.final()
}

private fun generateUUIDByHash(hashBytes: ByteArray, version: Int): UUID {
    hashBytes[6] = (hashBytes[6] and 0x0f or (version shl 4).toByte())
    hashBytes[8] = hashBytes[8] and 0x3f or 0x80.toByte()

    return UUID(hashBytes.copyOf(UUID_BYTE_ARRAY_SIZE))
}
