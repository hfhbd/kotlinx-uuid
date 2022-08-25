package kotlinx.uuid

import kotlinx.cinterop.*
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
        var result = 0
        for (byte in bytes) {
            result = (result or byte.toInt()) shl Byte.SIZE_BITS
        }
        return result
    }
}

private const val NTSTATUS_SUCCESS: NTSTATUS = 0
