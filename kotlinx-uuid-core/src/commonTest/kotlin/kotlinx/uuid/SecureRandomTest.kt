package kotlinx.uuid

import kotlin.test.*

class SecureRandomTestLinux {
    @Test
    fun random() {
        repeat(10) {
            val result = List(1000) {
                SecureRandom.nextLong()
            }.toSet()
            assertTrue(result.size in 990..1000, message = "$it: ${result.size}")
        }
    }
}
