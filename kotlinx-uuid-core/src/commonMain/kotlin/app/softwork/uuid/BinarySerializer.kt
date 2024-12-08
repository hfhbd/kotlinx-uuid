/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.Uuid

/**
 * This serializer is useful with binary formats to reduce size. You may also use it with
 * text formats like json, but it is not that useful (because there will be no size gain)
 * and a serialized Uuid is not human-readable.
 *
 * With this serializer, a Uuid is represented as an array of long with two elements.
 *
 * Example:
 * ```kotlin
 * Cbor.encodeToByteArray(BinarySerializer, myUUID)
 * ```
 */
public data object BinarySerializer : KSerializer<Uuid> {
    private val serializer = LongArraySerializer()
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: Uuid) {
        value.toLongs { mostSignificantBits, leastSignificantBits ->
            encoder.encodeSerializableValue(serializer, longArrayOf(mostSignificantBits, leastSignificantBits))
        }
    }

    override fun deserialize(decoder: Decoder): Uuid {
        val array = decoder.decodeSerializableValue(serializer)
        if (array.size != 2) {
            throw SerializationException("Uuid array should consist of 2 elements")
        }

        return Uuid.fromLongs(array[0], array[1])
    }
}
