/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.ktor.tests

import io.ktor.util.*
import kotlinx.uuid.*
import kotlinx.uuid.ktor.*
import java.lang.reflect.*
import kotlin.test.*

internal const val SOME_UUID_STRING: String = "1b3e4567-e99b-13d3-a476-446657420000"

class ConversionTest {
    private val type: Type = UUID::class.java

    @Test
    fun smokeToValuesTest() {
        assertEquals(listOf(SOME_UUID_STRING), UUIDConversionService.toValues(UUID(SOME_UUID_STRING)))
    }

    @Test
    fun toValuesNull() {
        assertEquals(listOf(), UUIDConversionService.toValues(null))
    }

    @Test
    fun toValuesWrongValue() {
        assertFailsWith<DataConversionException> {
            UUIDConversionService.toValues("ok")
        }
    }

    @Test
    fun smokeFromValuesTest() {
        assertEquals(UUID(SOME_UUID_STRING), UUIDConversionService.fromValues(listOf(SOME_UUID_STRING), type))
    }

    @Test
    fun nullFromValues() {
        assertEquals(null, UUIDConversionService.fromValues(emptyList(), type))
    }

    @Test
    fun tooManyElements() {
        assertFailsWith<DataConversionException> {
            UUIDConversionService.fromValues(listOf(SOME_UUID_STRING, SOME_UUID_STRING), type)
        }
    }

    @Test
    fun wrongTypeNotSupported() {
        assertFailsWith<DataConversionException> {
            UUIDConversionService.fromValues(listOf(SOME_UUID_STRING), String::class.java)
        }
    }

    @Test
    fun wrongUUIDFormat() {
        assertFailsWith<DataConversionException> {
            UUIDConversionService.fromValues(listOf("this is not UUID string"), type)
        }
        assertFailsWith<DataConversionException> {
            UUIDConversionService.fromValues(listOf(SOME_UUID_STRING.replace('b', 'X')), type)
        }
    }
}
