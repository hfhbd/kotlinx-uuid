package kotlinx.uuid.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.uuid.UUIDExperimentalAPI
import kotlinx.uuid.UUIDv7
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(UUIDExperimentalAPI::class)
class InstantTest {

    @Test
    fun testConversionInstant() {
        val timestamp = Clock.System.now()
        val expected = timestamp.clampToMillisecondPrecision()

        val uuid = UUIDv7(timeStamp = timestamp.toEpochMilliseconds())
        val result = uuid.instant

        assertEquals(expected, result)
    }

    private fun Instant.clampToMillisecondPrecision(): Instant {
        return Instant.fromEpochMilliseconds(this.toEpochMilliseconds())
    }
}