/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:JvmName("UUIDModule")

package kotlinx.uuid.jackson

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.*
import kotlinx.uuid.*

/**
 * Registers [uuidModule] in the mapper that provides the ability to
 * serialize and deserialize [UUID] as string primitives.
 */
public fun ObjectMapper.uuid(): ObjectMapper {
    return registerModule(uuidModule)
}

/**
 * A Jackson module providing the ability to
 * serialize and deserialize [UUID] as string primitives.
 */
public val uuidModule: Module
    get() = SimpleModule("UUID").apply {
        addSerializer(UUID::class.java, UUIDJacksonSerializer())
        addDeserializer(UUID::class.java, UUIDJacksonDeserializer())
    }
