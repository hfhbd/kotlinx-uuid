package app.softwork.uuid

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

class Uuidv7Test {
    @Test
    fun test() {
        val uuid = Uuid.v7(1645557742000, random = Random(4242))

        assertEquals(1645557742000, uuid.unixTimeStamp)
        assertEquals("017f22e2-79b0-793f-932c-d45d71287823", uuid.toString())
        assertEquals(7, uuid.version)
        assertEquals(4, uuid.variant) // Kotlin UUID exposes IETF variant as 0b100 (=4)
    }

    @Test
    fun testBasicGeneration() {
        val uuid = Uuid.v7(1645557742000)

        assertEquals(1645557742000, uuid.unixTimeStamp)
        assertEquals(7, uuid.version)
        assertTrue(uuid.variant == 5 || uuid.variant == 4) // Kotlin UUID exposes IETF variant as 0b100 (=4) or 0b101 (=5)
    }

    @Test
    @OptIn(ExperimentalTime::class)
    fun testConversionInstant() {
        val timestamp = Instant.parse("2020-02-20T10:21:42Z")
        val uuid = Uuid.v7(timeStamp = timestamp, random = Random(4242))
        assertEquals(timestamp, uuid.instant)
        assertEquals("0170621e-0ef0-793f-932c-d45d71287823", uuid.toString())
        assertEquals(7, uuid.version)
        assertEquals(4, uuid.variant) // Kotlin UUID exposes IETF variant as 0b100 (=4)
    }
}
