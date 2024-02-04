package kotlinx.uuid

import kotlinx.cinterop.*
import kotlinx.uuid.internal.*
import platform.Security.*
import platform.darwin.*
import kotlin.random.*

public actual val SecureRandom: Random = SecureRandomIos

/**
 * https://developer.apple.com/documentation/security/1399291-secrandomcopybytes?language=objc
 */
@OptIn(ExperimentalForeignApi::class)
private object SecureRandomIos : Random() {
    @OptIn(UnsafeNumber::class)
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val numberOfBytes = (bitCount + Byte.SIZE_BITS) / Byte.SIZE_BITS
        val bytes = ByteArray(size = numberOfBytes)
        val status = SecRandomCopyBytes(kSecRandomDefault, numberOfBytes.convert(), bytes.refTo(0))

        require(status == errSecSuccess)
        return bytes.toInt()
    }
}
