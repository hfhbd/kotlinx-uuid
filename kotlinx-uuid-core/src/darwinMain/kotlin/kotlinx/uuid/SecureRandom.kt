package kotlinx.uuid

import kotlinx.cinterop.*
import platform.Security.*
import platform.darwin.*
import kotlin.random.*

public actual val SecureRandom: Random = SecureRandomIos

/**
 * https://developer.apple.com/documentation/security/1399291-secrandomcopybytes?language=objc
 */
private object SecureRandomIos : Random() {
    override fun nextBits(bitCount: Int): Int {
        require(bitCount > 0)
        val numberOfBytes = (bitCount + Byte.SIZE_BITS) / Byte.SIZE_BITS
        val bytes = ByteArray(size = numberOfBytes)
        val status = SecRandomCopyBytes(kSecRandomDefault, numberOfBytes.convert(), bytes.refTo(0))

        require(status == errSecSuccess)
        var result = 0
        for (byte in bytes) {
            result = (result or byte.toInt()) shl Byte.SIZE_BITS
        }
        return result
    }
}
