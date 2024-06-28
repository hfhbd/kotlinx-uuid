package kotlinx.uuid.datetime

import kotlinx.datetime.Instant
import kotlinx.uuid.UUIDExperimentalAPI
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(UUIDExperimentalAPI::class)
class InstantTest {

    @Test
    fun testConversionInstant() {
        val timestamp = Instant.parse("2020-02-20T10:21:42Z")
        val uuid = UUIDv7(timeStamp = timestamp)
        assertEquals(timestamp, uuid.instant)
    }
}
