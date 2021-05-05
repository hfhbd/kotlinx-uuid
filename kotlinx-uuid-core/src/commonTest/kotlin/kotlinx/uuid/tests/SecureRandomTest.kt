package kotlinx.uuid.tests

import kotlinx.uuid.*
import kotlin.test.*

class SecureRandomTest {
    @Test
    @Ignore
    fun print() {
        val result = List(10) {
            SecureRandom.nextLong()
        }
        assertEquals(listOf(), result)
    }
}
