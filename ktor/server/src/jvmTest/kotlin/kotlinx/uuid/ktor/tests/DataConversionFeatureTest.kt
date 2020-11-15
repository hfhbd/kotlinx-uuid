/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.ktor.tests

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.ktor.util.*
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

    @Test
    @Ignore
    @OptIn(KtorExperimentalAPI::class)
    // this is still not working: ktor doesn't pick up conversion services in delegation
    fun testCallParameterDecoding(): Unit = withTestApplication {
        application.install(DataConversion) {
            uuid()
        }

        application.routing {
            get("/{uuid}") {
                val uuid: UUID by call.parameters
                call.respondText(uuid.toString())
            }
        }

        handleRequest(HttpMethod.Get, "/$SOME_UUID_STRING").let { call ->
            assertEquals(SOME_UUID_STRING, call.response.content)
        }
    }
}
