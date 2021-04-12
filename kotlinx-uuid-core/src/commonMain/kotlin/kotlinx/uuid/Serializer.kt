/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * This is the default [UUID] serializer that encodes instances as primitive strings
 * consisting of the canonical UUID string format.
 *
 * @property includeBrackets specifies, if serialized UUID should be wrapped into curly brackets
 */
public sealed class Serializer(
    private val includeBrackets: Boolean
) : KSerializer<UUID> {
    final override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    final override fun deserialize(decoder: Decoder): UUID {
        return UUID(decoder.decodeString())
    }

    final override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString(includeBrackets))
    }

    /**
     * The default serializer instance that encodes without curly brackets.
     */
    public object Default : Serializer(false)

    /**
     * This serializer encodes [UUID] with curly brackets
     */
    public object WrappedCurlyBrackets : Serializer(true)
}
