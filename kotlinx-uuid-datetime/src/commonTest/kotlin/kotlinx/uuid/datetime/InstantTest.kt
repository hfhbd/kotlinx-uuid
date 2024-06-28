package kotlinx.uuid.datetime

import kotlinx.datetime.Instant
import kotlinx.uuid.UUIDExperimentalAPI
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(UUIDExperimentalAPI::class)
class InstantTest {

    @Test
    fun testConversionInstant() {
        val timestamp = Instant.parse("2020-02-20T10:21:42Z")
        val uuid = UUIDv7(timeStamp = timestamp, random = Random(4242))
        assertEquals(timestamp, uuid.instant)
        assertEquals("0170621e-0ef0-7493-a342-1cdb22b5d84b", uuid.toString())
        assertEquals(7, uuid.versionNumber)
    }
}
