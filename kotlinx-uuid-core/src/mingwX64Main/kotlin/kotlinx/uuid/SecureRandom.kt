package kotlinx.uuid

import kotlinx.cinterop.*
import kotlinx.uuid.internal.*
import platform.windows.*
import kotlin.random.*

public actual val SecureRandom: Random = BCryptRandom()

private class BCryptRandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val numberOfBytes = (bitCount + Byte.SIZE_BITS) / Byte.SIZE_BITS
        val bytes = ByteArray(size = numberOfBytes)
        val status = bytes.usePinned {
            BCryptGenRandom(
                hAlgorithm = null,
                pbBuffer = it.addressOf(0).reinterpret(),
                cbBuffer = numberOfBytes.toUInt(),
                dwFlags = BCRYPT_USE_SYSTEM_PREFERRED_RNG
            )
        }

        require(status == NTSTATUS_SUCCESS)
        return bytes.toInt()
    }
}

private const val NTSTATUS_SUCCESS: NTSTATUS = 0
