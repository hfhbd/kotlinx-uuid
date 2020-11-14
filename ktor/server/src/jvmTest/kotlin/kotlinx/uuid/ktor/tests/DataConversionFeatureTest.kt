/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.ktor.tests

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.testing.*
import kotlinx.uuid.*
import kotlinx.uuid.ktor.*
import kotlin.test.*

class DataConversionFeatureTest {
    @Test
    fun smokeTest(): Unit = withTestApplication {
        application.install(DataConversion) {
            uuid()
        }

        val feature = application.feature(DataConversion)
        assertEquals(UUID(SOME_UUID_STRING), feature.fromValues(listOf(SOME_UUID_STRING), UUID::class.java))
        assertEquals(listOf(SOME_UUID_STRING), feature.toValues(UUID(SOME_UUID_STRING)))
    }
}
