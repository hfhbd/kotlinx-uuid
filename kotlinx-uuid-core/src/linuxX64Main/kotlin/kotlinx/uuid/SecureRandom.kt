package kotlinx.uuid

import kotlinx.cinterop.*
import platform.posix.*
import kotlin.random.*

public actual val SecureRandom: Random = DevUrandom()

private class DevUrandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val urandom = open("/dev/urandom", O_RDONLY)
        require(urandom >= 0)
        val (status, random) =  memScoped {
            val result: IntVar = alloc()
            read(urandom, result.ptr, bitCount.toULong()) to result.value
        }
        require(status >= 0)
        val result = random ushr (32 - bitCount)
        close(urandom)
        return result
    }
}
