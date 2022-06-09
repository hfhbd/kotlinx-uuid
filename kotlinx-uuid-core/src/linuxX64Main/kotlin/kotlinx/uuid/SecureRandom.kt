package kotlinx.uuid

import platform.posix.*
import kotlin.random.*

public actual val SecureRandom: Random = GetRandom()

private class GetRandom: Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val random = random()
        return (random ushr (32 - bitCount)).toInt()
    }
}
