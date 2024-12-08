package app.softwork.uuid

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class Uuidv7Test {
    @Test
    fun test() {
        val uuid = Uuidv7(1645557742000, random = Random(4242))

        assertEquals(1645557742000, uuid.unixTimeStamp)
        assertEquals("017f22e2-79b0-7a1e-ad73-08cd2f61d78a", uuid.toString())
        assertEquals(7, uuid.version)
        assertEquals(5, uuid.variant)
    }
}
