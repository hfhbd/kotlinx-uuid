package kotlinx.uuid

import kotlinx.cinterop.*
import kotlinx.uuid.internal.*
import platform.posix.*
import kotlin.random.*

public actual val SecureRandom: Random = DevUrandom()

private class DevUrandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val numberOfBytes = (bitCount + Byte.SIZE_BITS) / Byte.SIZE_BITS
        val bytes = ByteArray(size = numberOfBytes)
        val urandom = open("/dev/urandom", O_RDONLY)
        require(urandom >= 0)
        val status = bytes.usePinned {
            read(urandom, it.addressOf(0), numberOfBytes.convert())
        }
        close(urandom)
        require(status >= 0)
        return bytes.toInt()
    }
}
