/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlin.uuid.Uuid

/**
 * This serializer is useful with binary formats to reduce size. You may also use it with
 * text formats like json, but it is not that useful (because there will be no size gain)
 * and a serialized UUID is not human-readable.
 *
 * With this serializer, a UUID is represented as an array of long with two elements.
 *
 * Example:
 * ```kotlin
 * Cbor.encodeToByteArray(BinarySerializer, myUUID)
 * ```
 */
public object BinarySerializer : KSerializer<Uuid> {
    private val serializer = LongArraySerializer()
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: Uuid) {
        value.toLongs { mostSignificantBits, leastSignificantBits ->
            encoder.encodeSerializableValue(serializer, longArrayOf(mostSignificantBits, leastSignificantBits))
        }
    }

    override fun deserialize(decoder: Decoder): Uuid {
        return decoder.decodeSerializableValue(serializer).let { array ->
            if (array.size != 2) {
                throw SerializationException("UUID array should consist of 2 elements")
            }

            Uuid.fromLongs(array[0], array[1])
        }
    }
}
