/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.jackson

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.jsonFormatVisitors.*
import com.fasterxml.jackson.databind.ser.std.*
import kotlinx.uuid.*
import java.lang.reflect.*

internal class UUIDJacksonSerializer(type: Class<UUID>) : StdSerializer<UUID>(type) {
    constructor() : this(UUID::class.java)

    override fun serialize(value: UUID?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
            return
        }

        gen.writeString(value.toString())
    }

    override fun getSchema(provider: SerializerProvider?, typeHint: Type?): JsonNode {
        return createSchemaNode("string", false)
    }

    override fun acceptJsonFormatVisitor(visitor: JsonFormatVisitorWrapper, typeHint: JavaType?) {
        visitor.expectStringFormat(typeHint)
    }
}
