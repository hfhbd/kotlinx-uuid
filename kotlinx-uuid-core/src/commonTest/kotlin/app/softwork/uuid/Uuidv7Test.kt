package app.softwork.uuid

import kotlin.random.Random
import kotlin.test.*

class Uuidv7Test {
    @Test
    fun test() {
        val one = Uuidv7(0x17F22E279B0, random = Random(4242))
        val two = Uuidv7(1645557742000, random = Random(4242))
        assertEquals(one, two)

        assertEquals(1645557742000, one.unixTimeStamp)
        assertEquals(1645557742000, two.unixTimeStamp)
        assertEquals("017f22e2-79b0-7b35-ab5c-c2334bd875e2", one.toString())
        assertEquals(7, one.versionNumber)
    }
}
