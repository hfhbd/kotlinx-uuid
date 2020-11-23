/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.jackson

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.*
import kotlinx.uuid.*

internal class UUIDJacksonDeserializer(type: Class<UUID>) : StdDeserializer<UUID>(type) {
    constructor() : this(UUID::class.java)

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): UUID? {
        if (p == null) {
            return null
        }
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return UUID(p.text)
        }
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            return null
        }

        ctxt?.handleUnexpectedToken(UUID::class.java, p)
        return null
    }
}
