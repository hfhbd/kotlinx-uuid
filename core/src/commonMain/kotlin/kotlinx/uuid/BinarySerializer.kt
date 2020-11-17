/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * This serializer is useful with binary formats to reduce size. You may also use it with
 * text formats like json but it is not that useful (because there will be no size gain)
 * and a serialized UUID is not human-readable.
 *
 * With this serializer, a UUID is represented as an array of long with two elements.
 *
 * Example:
 * ```kotlin
 * Cbor.encodeToByteArray(BinarySerializer, myUUID)
 * ```
 */
public object BinarySerializer : KSerializer<UUID> {
    private val serializer = serializer<LongArray>()
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeSerializableValue(
            serializer,
            longArrayOf(value.timeStampAndVersionRaw, value.clockSequenceVariantAndNodeRaw)
        )
    }

    override fun deserialize(decoder: Decoder): UUID {
        return decoder.decodeSerializableValue(serializer).let { array ->
            if (array.size != 2) {
                throw SerializationException("UUID array should consist of 2 elements")
            }

            UUID.create(
                array[0],
                array[1]
            )
        }
    }
}
