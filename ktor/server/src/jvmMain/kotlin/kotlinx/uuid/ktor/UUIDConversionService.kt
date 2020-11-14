/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.ktor

import io.ktor.util.*
import kotlinx.uuid.*
import java.lang.reflect.*

/**
 * ktor [ConversionService] implementation that does conversion for [UUID]
 */
public object UUIDConversionService : ConversionService {
    override fun fromValues(values: List<String>, type: Type): Any? {
        if (type == UUID::class.java) {
            return when (values.size) {
                0 -> null
                1 -> fromValue(values[0])
                else -> throw DataConversionException("Multiple UUID values can't be converted to a single UUID")
            }
        }

        throw DataConversionException("Type $type is not supported by this ConversionService")
    }

    private fun fromValue(value: String): UUID = try {
        UUID(value)
    } catch (cause: Throwable) {
        throw DataConversionException(cause.message ?: "Failed to convert UUID: $cause")
    }

    override fun toValues(value: Any?): List<String> {
        return when (value) {
            is UUID -> listOf(value.toString())
            null -> emptyList()
            else -> throw DataConversionException("Value $value is not supported by UUIDConversionService")
        }
    }
}
