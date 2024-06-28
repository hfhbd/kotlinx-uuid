package kotlinx.uuid

import kotlin.random.Random
import kotlin.test.*

@UUIDExperimentalAPI
class UUIDv7Test {
    @Test
    fun test() {
        val one = UUIDv7(0x17F22E279B0, random = Random(4242))
        val two = UUIDv7(1645557742000, random = Random(4242))
        assertEquals(one, two)

        assertEquals(1645557742000, one.unixTimeStamp)
        assertEquals(1645557742000, two.unixTimeStamp)
        assertEquals("017f22e2-79b0-7493-a342-1cdb22b5d84b", one.toString())
        assertEquals(7, one.versionNumber)
    }
}
