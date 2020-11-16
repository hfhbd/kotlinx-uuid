/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.gson

import com.google.gson.*
import kotlinx.uuid.*
import java.lang.reflect.*

/**
 * Provides ability to serialize/deserialize [UUID] for Gson.
 *
 * It's recommended to register it via function [registerUUID] instead of direct usage.
 *
 * ```kotlin
 * GsonBuilder().registerUUID()
 * ```
 */
public object UUIDGsonSerializer : JsonSerializer<UUID>, JsonDeserializer<UUID> {
    override fun serialize(src: UUID?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        if (src == null) {
            return JsonNull.INSTANCE
        }

        return JsonPrimitive(src.toString())
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): UUID? {
        return when (json) {
            null, is JsonNull -> null
            is JsonPrimitive -> UUID(json.asString)
            else -> throw JsonParseException("Failed to deserialize json element $json: should be a primitive string.")
        }
    }
}
