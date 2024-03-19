package kotlinx.uuid

import kotlinx.browser.*
import kotlinx.uuid.internal.*
import org.khronos.webgl.*
import org.w3c.dom.*
import kotlin.random.*

private val isNode =
    js("typeof process !== 'undefined' && process.versions != null && process.versions.node != null") as Boolean

public actual val SecureRandom: Random = if (isNode) SecureRandomNode else SecureRandomBrowser

private external interface Crypto {
    fun getRandomValues(array: Uint32Array): Uint32Array
}

private object SecureRandomBrowser : Random() {
    private inline val Window.crypto: Crypto
        get() = asDynamic().crypto.unsafeCast<Crypto>()

    override fun nextBits(bitCount: Int): Int =
        nextInt().takeUpperBits(bitCount)

    override fun nextLong(): Long {
        val randomInts = window.crypto.getRandomValues(Uint32Array(2))
        return ((randomInts[0].toULong() shl Int.SIZE_BITS) + randomInts[1].toULong()).toLong()
    }

    override fun nextInt(): Int = window.crypto.getRandomValues(Uint32Array(1))[0]

    /**
     * Copied from [stdLib][kotlin.random.takeUpperBits]
     */
    private fun Int.takeUpperBits(bitCount: Int): Int =
        this.ushr(32 - bitCount) and (-bitCount).shr(31)
}

private external interface CryptoNode {
    fun randomInt(max: Int): Int
    fun randomBytes(bytes: Int): ArrayBuffer
}

private object SecureRandomNode : Random() {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val crypto: CryptoNode = js("eval('require')('crypto')") as CryptoNode

    override fun nextBits(bitCount: Int): Int {
        val numberOfBytes = (bitCount + Byte.SIZE_BITS) / Byte.SIZE_BITS
        val random = crypto.randomBytes(numberOfBytes)
        return Int8Array(random).unsafeCast<ByteArray>().toInt()
    }

    override fun nextInt(): Int {
        return crypto.randomInt(max = Int.MAX_VALUE)
    }
}
