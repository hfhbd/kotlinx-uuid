package app.softwork.uuid

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

@Suppress("DEPRECATION")
class Uuidv7Test {
    @Test
    fun test() {
        val uuid = Uuidv7(1645557742000, random = Random(4242))

        assertEquals(1645557742000, uuid.unixTimeStamp)
        assertEquals("017f22e2-79b0-7a1e-ad73-08cd2f61d78a", uuid.toString())
        assertEquals(7, uuid.version)
        assertEquals(5, uuid.variant)
    }

    @Test
    fun testWithBase() {
        val base = Random(4242).nextUuid()
        val uuid = Uuidv7(1645557742000, base)

        assertEquals(1645557742000, uuid.unixTimeStamp)
        assertEquals(7, uuid.version)
        assertEquals(5, uuid.variant)
        assertEquals("017f22e2-79b0-7a1e-ad73-08cd2f61d78a", uuid.toString())
    }

    @Test
    fun testConversionInstant() {
        val timestamp = Instant.parse("2020-02-20T10:21:42Z")
        val uuid = Uuidv7(timeStamp = timestamp, random = Random(4242))
        assertEquals(timestamp, uuid.instant)
        assertEquals("0170621e-0ef0-7a1e-ad73-08cd2f61d78a", uuid.toString())
        assertEquals(7, uuid.version)
    }
}
