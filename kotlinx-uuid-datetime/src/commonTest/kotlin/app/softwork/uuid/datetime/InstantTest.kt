package app.softwork.uuid.datetime

import app.softwork.uuid.versionNumber
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class InstantTest {

    @Test
    fun testConversionInstant() {
        val timestamp = Instant.parse("2020-02-20T10:21:42Z")
        val uuid = Uuidv7(timeStamp = timestamp, random = Random(4242))
        assertEquals(timestamp, uuid.instant)
        assertEquals("0170621e-0ef0-7a1e-ad73-08cd2f61d78a", uuid.toString())
        assertEquals(7, uuid.versionNumber)
    }
}
